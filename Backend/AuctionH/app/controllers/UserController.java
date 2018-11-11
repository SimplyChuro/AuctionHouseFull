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
	
	//API GET all users  ------ Testing only
	public Result getAll() {
		try {
			List<Users> users = Users.find.all();
			return ok(Json.toJson(users));
		}catch(Exception e) {
			return notFound();
		}
	}

	//	inactive
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
			
			Users userChecker = Users.find.query().where().conjunction().eq("email", userNode.findPath("email").textValue()).endJunction().findUnique();
			if(userChecker != null) {
				return badRequest();
			} else {
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
			
			JsonNode userNode = jsonNode.get("user");
			
			Users userChecker = Users.find.query().where().conjunction().eq("email", userNode.findPath("email").textValue()).endJunction().findUnique();
			if(userChecker != null) {
				userChecker = Json.fromJson(userNode, Users.class);
				userChecker.setPassword(userNode.findValue("password").asText());
				userChecker.updateUser();
				return ok();
			} else {
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
		
			if(!user.getEmailVerified()) {
				user.setEmailVerified(true);
				user.update();
				
				return ok();
			}else {
				return notFound();
			}
		}catch(Exception e) {
			return badRequest();
		}
	} 	
	
	
}
