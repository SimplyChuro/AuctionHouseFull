package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;

import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class LoginController extends Controller {
	
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
	    
//	    if(user.emailVerified == true) {
		    if (BCrypt.checkpw(jsonNode.findPath("password").textValue(), user.getPassword())) {
	    		
		    	ArrayNode response;
	    		
		    	ObjectNode authTokenJson;
		    	ObjectNode userNode;
		    	
		    	if(user.hasAuthToken()) {
		    		authTokenJson = Json.newObject();
		    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
		    		
		    		userNode = Json.newObject();
		    		userNode.put("userID", user.id);
		    		
		    		response = Json.newArray();
		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		
		    		return ok(response);
		    	} else {
			        String authToken = user.createToken();
			        
			        authTokenJson = Json.newObject();
			        authTokenJson.put(AUTH_TOKEN, authToken);
			        
		    		userNode = Json.newObject();
		    		userNode.put("userID", user.id);
		    		
		    		response = Json.newArray();
		    		response.add(authTokenJson);
		    		response.add(userNode);
		    		
			        return ok(response);
		        }
//		    } else {
//	            return badRequest(Json.toJson("In order to login you have to verify your account!"));	
//		    }
        } else {
            return notFound(Json.toJson(""));
    	}
	    
	}
	
	@Security.Authenticated(Secured.class)
	public Result logout() {
		Users user = getUser();
//		if(user.emailVerified == true) {
			user.deleteAuthToken();
		    return ok();
//		} else {
//            return badRequest();	
//		}
    }

}