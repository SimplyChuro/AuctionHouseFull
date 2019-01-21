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


import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;
import java.io.File;
import org.apache.commons.mail.EmailAttachment;

public class UserController extends Controller {
	@Inject MailerClient mailerClient;
	
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
				
				String token;
				if(user.hasAuthToken()) {
					token = user.getAuthToken();
				} else {
					user.createToken();
					token = user.getAuthToken();
				}
				
				Email email = new Email()
				      .setSubject("Verify your mail!")
				      .setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
				      .addTo("Misster/Miss <"+user.getEmail()+">")
				      .setBodyText("A text message")
				      .setBodyHtml(
				    		  "<html>"
				      		+ "<body>"
				      		+ "<p>"
				      		+ "In order to verify your email click the following link :"
				      		+ "</p>"
				      		+ "<a href=http://localhost:4200/verify-email?token="+token+">Verify Email</a>"
				      		+ "</body>"
				      		+ "</html>");
			    mailerClient.send(email);
					    
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
	
	//DELETE user
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			JsonNode objectNode = request().body().asJson().get("user");
			Users user = LoginController.getUser();
			user.delete();
			return ok(Json.toJson(""));
		} catch(Exception e) {
			return badRequest();
		}
	}

	//Verify mail
	@Security.Authenticated(Secured.class)
	public Result verify() {
		try {
			Users userChecker = LoginController.getUser();
		
			if(!userChecker.getEmailVerified()) {
				userChecker.setEmailVerified(true);
				userChecker.update();
				
				return ok(Json.toJson(""));
			} else {
				return notFound();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Reset password
	@Security.Authenticated(Secured.class)
	public Result reset() {
		try {
			JsonNode objectNode = request().body().asJson();
			Users userChecker = LoginController.getUser();
			
			if(userChecker != null) {
				userChecker.updatePassword(objectNode);
				userChecker.deleteAuthToken();
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
		}catch(Exception e) {
			return badRequest();
		}
	} 
	
	//Reset password mail
	public Result sendResetMail() {
		try {
			JsonNode objectNode = request().body().asJson();
			
			Users userChecker = Users.find.query().where()
					.conjunction()
					.eq("email", objectNode.findPath("email").textValue())
					.endJunction()
					.findUnique();
			
			if(userChecker != null) {
				
				String token;
				if(userChecker.hasAuthToken()) {
					token = userChecker.getAuthToken();
				} else {
					userChecker.createToken();
					token = userChecker.getAuthToken();
				}
				
				Email email = new Email()
					.setSubject("Password reset!")
					.setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
					.addTo("Misster/Miss <"+objectNode.findPath("email").textValue()+">")
					.setBodyText("Password Reset Guide")
					.setBodyHtml(
				    		  "<html>"
				      		+ "<body>"
				      		+ "<p>"
				      		+ "In order to reset your password click the following link :"
				      		+ "</p>"
				      		+"<br>"
				      		+ "<a href=http://localhost:4200/new-password?token="+token+">Password Reset Link</a>"
				      		+ "</body>"
				      		+ "</html>");
			    mailerClient.send(email);
			    
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
		}catch(Exception e) {
			return badRequest();
		}
	}
		
}