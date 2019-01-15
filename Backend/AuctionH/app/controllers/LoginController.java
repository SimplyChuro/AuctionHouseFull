package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
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
	
	    Users user = Users.find.query().where()
	    		.conjunction()
	    		.eq("email", jsonNode.findPath("email").textValue())
	    		.endJunction()
	    		.findUnique();	 
	    
	    ObjectNode errorNode;
	    
	    if (BCrypt.checkpw(jsonNode.findPath("password").textValue(), user.getPassword())) {
	    	ArrayNode response = Json.newArray();
	    	
	    	ObjectNode authTokenJson;
	    	ObjectNode userNode;
	    	ObjectNode adminNode;
	    	
	    	if(user.active) {
			    if(user.emailVerified == true) {
			    	if(user.hasAuthToken()) {
			    		authTokenJson = Json.newObject();
			    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
			    		
			    		userNode = Json.newObject();
			    		userNode.put("userID", user.id);
			    		
			    		adminNode = Json.newObject();
			    		adminNode.put("adminChecker", user.admin);
			    		
			    		response = Json.newArray();
			    		response.add(authTokenJson);
			    		response.add(userNode);
			    		response.add(adminNode);
			    		
			    		return ok(response);
			    	} else {
				        String authToken = user.createToken();
				        
				        authTokenJson = Json.newObject();
				        authTokenJson.put(AUTH_TOKEN, authToken);
				        
			    		userNode = Json.newObject();
			    		userNode.put("userID", user.id);
			    		
			    		adminNode = Json.newObject();
			    		adminNode.put("adminChecker", user.admin);
			    		
			    		response.add(authTokenJson);
			    		response.add(userNode);
			    		response.add(adminNode);
			    		
				        return ok(response);
			        }
			    } else {
			    	errorNode = Json.newObject();
			    	errorNode.put("error_message", "The given account has not been verified");
			    	
			    	return badRequest(errorNode);
			    }
		    } else {
		    	errorNode = Json.newObject();
		    	errorNode.put("error_message", "The given account has been deactivated, in order to reactivate the account we have sent you a reactivation mail");
		    	
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
		    	
		    	return badRequest(errorNode);
		    }
        } else {
        	errorNode = Json.newObject();
	    	errorNode.put("error_message", "Incorrect email or password");
	
	    	return notFound(errorNode);
    	}
	    
	}
	
	@Security.Authenticated(Secured.class)
	public Result logout() {
		Users user = getUser();
		if(user.emailVerified == true) {
			user.deleteAuthToken();
		    return ok(Json.toJson(""));
		} else {
            return badRequest();	
		}
    }

}