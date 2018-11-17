package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	    return (Users)Http.Context.current().args.get("user");
	}
	
	// returns an authToken
	public Result login() {
	    JsonNode jsonNode = request().body().asJson();
	
	    Users user = Users.find.query().where().conjunction()
	    		.eq("email", jsonNode.findPath("email").textValue())
	    		.eq("password", jsonNode.findPath("password").textValue())
	    		.endJunction()
	    		.findUnique();	    
	    
	    if (user == null) {
	        return unauthorized();
	    } else {
	    	ObjectNode authTokenJson;
	    	
	    	if(user.hasAuthToken()) {
	    		authTokenJson = Json.newObject();
	    		authTokenJson.put(AUTH_TOKEN, user.getAuthToken());
	    		return ok(authTokenJson);
	    	}else {
		        String authToken = user.createToken();
		        authTokenJson = Json.newObject();
		        authTokenJson.put(AUTH_TOKEN, authToken);
		        return ok(authTokenJson);
	        }
	    }
	}
	
	@Security.Authenticated(Secured.class)
	public Result logout() {
	    getUser().deleteAuthToken();
	    return ok();
    }

}