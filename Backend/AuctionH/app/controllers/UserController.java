package controllers;

import java.util.Iterator;
import play.data.validation.*;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jdk.nashorn.internal.objects.annotations.Where;
import models.Products;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Address;
import models.Bids;
import models.Sales;
import models.Wishlists;
import models.Users;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {

	@Inject FormFactory formFactory;
	
	public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
	public static final String AUTH_TOKEN = "authToken";
	
	//API GET all users  ------ Testing only
	public Result users() {
		try {
			List<Users> users = Users.find.all();
			return ok(Json.toJson(users));
		}catch(Exception e) {
			return notFound();
		}
	}
	

	public Result user() {
		try {
			JsonNode jsonNode = request().body().asJson();
			Users user = Users.findByAuthToken(jsonNode.findPath("authToken").asText());
			return ok(Json.toJson(user));
		}catch(Exception e) {
			return notFound();
		}
	}
	
	
	//create user	
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			JsonNode userNode = jsonNode.get("user");
			try {
				Users userChecker = Users.find.query().where().conjunction().eq("email", userNode.findPath("email").textValue()).endJunction().findList().get(0);
				return badRequest();
			} catch(Exception e) {
				Users user = Json.fromJson(userNode, Users.class);
				user.setPassword(userNode.findValue("password").asText());
				user.setBase();
				return ok();
			}
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update user
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Users userChecker = Users.find.query().where().conjunction().eq("email", jsonNode.findPath("email").textValue()).endJunction().findList().get(0);
				userChecker = Json.fromJson(jsonNode, Users.class);
				userChecker.updateUser();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}
		}catch(Exception e) {
			return badRequest();
		}
	}

	//Verify mail
	public Result verify(Long id) {
		try {
			Users user = Users.find.byId(id);
		
			if(!user.emailVerified) {
				user.emailVerified = true;
				user.update();
				
				return ok();
			}else {
				return notFound();
			}
		}catch(Exception e) {
			return badRequest();
		}
	} 	
	
	public static Users getUser() {
	    return (Users)Http.Context.current().args.get("user");
	}
	
	// returns an authToken
	public Result login() {
	    JsonNode jsonNode = request().body().asJson();
	
	    Users user = Users.find.query().where().conjunction().eq("email", jsonNode.findPath("email").textValue()).eq("password", jsonNode.findPath("password").textValue()).endJunction().findUnique();	    
	    
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
		        response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());
		        return ok(authTokenJson);
	        }
	    }
	}
	
	@Security.Authenticated(Secured.class)
	public Result logout() {
	    response().discardCookie(AUTH_TOKEN);
	    getUser().deleteAuthToken();
	    return ok();
    }
	
}
