package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;

import akka.http.javadsl.model.HttpResponse;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.mindrot.jbcrypt.BCrypt;

import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class LoginController extends Controller {
	
	@Inject MailerClient mailerClient;
	
	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public static final String AUTH_TOKEN = "authToken";
	
	public static Users getUser() {
	    return (Users) Http.Context.current().args.get("user");
	}
	
	// returns an authToken
	public Result login() {
	    JsonNode jsonNode = request().body().asJson();
	    ObjectNode errorNode = Json.newObject();
	    
	    try {
	    
		    Users user = Users.find.query().where()
		    		.conjunction()
		    		.eq("email", jsonNode.findPath("email").textValue())
		    		.endJunction()
		    		.findUnique();	 
		    
		    if (BCrypt.checkpw(jsonNode.findPath("password").textValue(), user.getPassword())) {
		    	ArrayNode response = Json.newArray();
		    	
		    	ObjectNode authTokenJson = Json.newObject();
		    	ObjectNode userNode = Json.newObject();
		    	ObjectNode adminNode = Json.newObject();
		    	
		    	if(user.active) {
				    if(user.emailVerified == true) {
				    	if(user.hasAuthToken()) {
				    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
				    		userNode.put("userID", user.id);
				    		adminNode.put("adminChecker", user.admin);

				    		response.add(authTokenJson);
				    		response.add(userNode);
				    		response.add(adminNode);
				    		
				    		return ok(response);
				    	} else {
					        String authToken = user.createToken();

					        authTokenJson.put(AUTH_TOKEN, authToken);
				    		userNode.put("userID", user.id);
				    		adminNode.put("adminChecker", user.admin);
				    		
				    		response.add(authTokenJson);
				    		response.add(userNode);
				    		response.add(adminNode);
				    		
					        return ok(response);
				        }
				    } else {
				    	errorNode.put("error_message", "The given account has not been verified");
				    	return badRequest(errorNode);
				    }
			    } else {
			    	Email email = new Email()
							.setSubject("Reactivate your account!")
							.setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
							.addTo("Misster/Miss <"+user.email+">")
							.setBodyText("Reactivation Email")
							.setBodyHtml(
						    		  "<html>"
						      		+ "<body>"
						      		+ "<p>"
						      		+ "In order to reactivate your account click the following link :"
						      		+ "</p>"
						      		+"<br>"
						      		+ "<a href=https://auction-house-frontend.herokuapp.com/reactivate-account?token="+user.createToken()+">Account Reactivation Link</a>"
						      		+ "</body>"
						      		+ "</html>");
				    mailerClient.send(email);
			    	
			    	errorNode.put("error_message", "The given account has been deactivated, in order to reactivate the account we have sent you a reactivation mail");
			    	return badRequest(errorNode);
			    }
	        } else {
		    	errorNode.put("error_message", "Incorrect email or password");
		    	return notFound(errorNode);
	    	}
	    } catch(Exception e) {
	    	errorNode.put("error_message", "Incorrect email or password");
	    	return notFound(errorNode);
	    }
	}
	
	
	public Result googleLogin() {
	    try {
		    JsonNode jsonNode = request().body().asJson();
		    JsonNode responseNode;
		    ArrayNode response = Json.newArray();
	    	
	    	ObjectNode authTokenJson = Json.newObject();;
	    	ObjectNode userNode = Json.newObject();;
	    	ObjectNode adminNode = Json.newObject();;
	    	
		    HttpClient httpClient = new DefaultHttpClient();
		    ObjectMapper mapper = new ObjectMapper();
		    BufferedReader reader;
		    HttpPost httpPostRequest;
		    HttpGet httpGetRequest;
		    String responseString = "";
		    String accessKey;
		    String authToken;
		    org.apache.http.HttpResponse httpResponse;
		    
	    	httpPostRequest = new HttpPost("https://accounts.google.com/o/oauth2/token");
	    	httpPostRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
	    	httpPostRequest.setHeader("X-Requested-With", "XMLHttpRequest");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("code", jsonNode.findPath("code").asText()));
			nameValuePairs.add(new BasicNameValuePair("client_id", ConfigFactory.load().getString("custom.oauth2.google.clientID")));
			nameValuePairs.add(new BasicNameValuePair("client_secret", ConfigFactory.load().getString("custom.oauth2.google.secret")));
			nameValuePairs.add(new BasicNameValuePair("redirect_uri", jsonNode.findPath("redirectURI").asText()));
			nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
			httpPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			httpResponse = httpClient.execute(httpPostRequest);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()), 2048);
			if (httpResponse != null) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        sb.append(line);
			    }
			    responseString = sb.toString();
			}
			
		    responseNode = mapper.readTree(responseString);
		    accessKey = responseNode.findPath("access_token").asText();
		    
		    httpGetRequest = new HttpGet("https://www.googleapis.com/oauth2/v3/userinfo");
		    httpGetRequest.setHeader("Authorization", "Bearer " + accessKey);
		    httpResponse = httpClient.execute(httpGetRequest);
		    
		    reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()), 2048);
			if (httpResponse != null) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        sb.append(line);
			    }
			    responseString = sb.toString();
			}
			
			responseNode = mapper.readTree(responseString);
		    
			Users user = Users.find.query().where()
		    		.conjunction()
		    		.eq("email", responseNode.findPath("email").textValue())
		    		.endJunction()
		    		.findUnique();
			
			if(user != null) {		
		    	if(user.hasAuthToken()) {
		    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
		    		userNode.put("userID", user.id);
		    		adminNode.put("adminChecker", user.admin);
		    		
		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		response.add(adminNode);
		    		
		    		return ok(response);
		    	} else {
			        authToken = user.createToken();
			        
			        authTokenJson.put(AUTH_TOKEN, authToken);
		    		userNode.put("userID", user.id);
		    		adminNode.put("adminChecker", user.admin);
		    		
		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		response.add(adminNode);
		    		
			        return ok(response);
		        }
			} else {
				user = new Users();
				user.setPassword(UUID.randomUUID().toString() + UUID.randomUUID().toString());
				user.setBaseGoogle(responseNode);
				
				authToken = user.createToken();
		        
		        authTokenJson.put(AUTH_TOKEN, authToken);
	    		userNode.put("userID", user.id);
	    		adminNode.put("adminChecker", user.admin);
	    		
	    		response.add(authTokenJson);
	    		response.add(userNode);
	    		response.add(adminNode);
	    		
		        return ok(response);
			}
	    } catch(Exception e) {
	    	return badRequest();
	    }
	}
	
	public Result facebookLogin() {
	    try {
	    	JsonNode jsonNode = request().body().asJson();
		    JsonNode responseNode;
		    ArrayNode response = Json.newArray();
	    	
	    	ObjectNode authTokenJson = Json.newObject();;
	    	ObjectNode userNode = Json.newObject();;
	    	ObjectNode adminNode = Json.newObject();;
	    	
		    HttpClient httpClient = new DefaultHttpClient();
		    ObjectMapper mapper = new ObjectMapper();
		    BufferedReader reader;
		    HttpGet httpGetRequest;
		    String responseString = "";
		    String accessKey;
		    String authToken;
		    org.apache.http.HttpResponse httpResponse;
		    
		    httpGetRequest = new HttpGet("https://graph.facebook.com/v3.2/oauth/access_token?"
	    			+ "client_id="+ConfigFactory.load().getString("custom.oauth2.facebook.clientID")+"&"
					+ "redirect_uri="+jsonNode.findPath("redirectURI").asText()+"&"
					+ "client_secret="+ConfigFactory.load().getString("custom.oauth2.facebook.secret")+"&"
					+ "code="+jsonNode.findPath("code").asText());
		    httpResponse = httpClient.execute(httpGetRequest);
		    
		    reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()), 2048);
			if (httpResponse != null) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        sb.append(line);
			    }
			    responseString = sb.toString();
			}
			
			responseNode = mapper.readTree(responseString);
		    accessKey = responseNode.findPath("access_token").asText();
	    	
		    httpGetRequest = new HttpGet("https://graph.facebook.com/me?fields=email,first_name,last_name,picture&access_token="+accessKey);
		    httpResponse = httpClient.execute(httpGetRequest);
		    
		    reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()), 2048);
			if (httpResponse != null) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        sb.append(line);
			    }
			    responseString = sb.toString();
			}
			
			responseNode = mapper.readTree(responseString);
		    
			Users user = Users.find.query().where()
		    		.conjunction()
		    		.eq("email", responseNode.findPath("email").textValue())
		    		.endJunction()
		    		.findUnique();
			
			if(user != null) {		
		    	if(user.hasAuthToken()) {
		    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
		    		userNode.put("userID", user.id);
		    		adminNode.put("adminChecker", user.admin);

		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		response.add(adminNode);
		    		
		    		return ok(response);
		    	} else {
			        authToken = user.createToken();

			        authTokenJson.put(AUTH_TOKEN, authToken);
		    		userNode.put("userID", user.id);
		    		adminNode.put("adminChecker", user.admin);
		    		
		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		response.add(adminNode);
		    		
			        return ok(response);
		        }
			} else {
				user = new Users();
				user.setPassword(UUID.randomUUID().toString() + UUID.randomUUID().toString());
				user.setBaseFacebook(responseNode);
				
				authToken = user.createToken();

		        authTokenJson.put(AUTH_TOKEN, authToken);
	    		userNode.put("userID", user.id);
	    		adminNode.put("adminChecker", user.admin);
	    		
	    		response.add(authTokenJson);
	    		response.add(userNode);
	    		response.add(adminNode);
	    		
		        return ok(response);
			}
	    } catch(Exception e) {
	    	return badRequest();
	    }
	}
	
	
	@Security.Authenticated(Secured.class)
	public Result logout() {
		try {
			Users user = getUser();
			if(user.emailVerified == true) {
				user.deleteAuthToken();
			    return ok(Json.toJson(""));
			} else {
	            return badRequest();
			}
		} catch (Exception e) {
			return badRequest();
		}
    }

}