package controllers;

import java.util.Iterator;
import play.data.validation.*;
import java.util.List;
import java.util.TimeZone;

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
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;

public class UserController extends Controller {

	//Get user
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			
			return ok(Json.toJson(user));
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//Get users
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			List<Users> users = Users.find.all();
			
			return ok(Json.toJson(users));
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//Create user	
	public Result create() {
		try {
			JsonNode objectNode = request().body().asJson().get("user");
			
			Users userChecker = Users.find.query().where()
					.conjunction()
					.eq("email", objectNode.findPath("email").textValue())
					.endJunction()
					.findUnique();
			
			if(userChecker != null) {
				return badRequest();
			} else {
				Users user = Json.fromJson(objectNode, Users.class);
				user.setPassword(objectNode.findValue("password").asText());
				user.setBase();
				return ok(Json.toJson(user));
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update user
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			JsonNode objectNode = request().body().asJson().get("user");
			Users user = LoginController.getUser();
			user.updateUser(objectNode);
			return ok(Json.toJson(user));
		} catch(Exception e) {
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
			} else {
				return notFound();
			}
		} catch(Exception e) {
			return badRequest();
		}
	} 	
	
	//Reset password not finished
	public Result reset() {
		try {
			JsonNode objectNode = request().body().asJson();
			Users userChecker = Users.find.query().where()
					.conjunction()
					.eq("email", objectNode.findPath("email").textValue())
					.endJunction()
					.findUnique();
			
			if(userChecker != null) {
//
//				SimpleEmail email = new SimpleEmail();
//	            email.setFrom(address);
//	            email.addTo("ss@gmail.com");
//	            email.setSubject("Reset Password");
//	            email.setMsg("Click the link bellow to reset your password");
//	            Mail.send(email);
				return ok();
			} else {
				return badRequest();
			}
		}catch(Exception e) {
			return badRequest();
		}
	} 
	
}