package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import play.data.validation.*;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jdk.nashorn.internal.objects.annotations.Where;
import models.Products;
import models.Reviews;
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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.mail.EmailAttachment;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;



public class UserController extends Controller {
	
	@Inject MailerClient mailerClient;
	
	//Get user
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			if(user.admin) {
				Users requestedUser = Users.find.byId(id);
				return ok(Json.toJson(requestedUser));
			} else {
				return ok(Json.toJson(user));
			}
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//Get users
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			
			if(user.admin) {
				List<Users> users = Users.find.query().where()
						.conjunction()
						.ge("id", 2)
						.endJunction()
						.findList();
				
				return ok(Json.toJson(users));	
			} else {
				return forbidden();
			}
			
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
				      		+ "<a href=https://auction-house-frontend.herokuapp.com/verify-email?token="+token+">Verify Email</a>"
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
			Users user = LoginController.getUser();
			JsonNode objectNode = request().body().asJson().get("user");
			if(user.admin) {
				Users userChecker = Users.find.byId(id);
//				userChecker.updatePassword(objectNode);
//				userChecker.updatePassword(objectNode);
				userChecker.updateUser(objectNode);
				return ok(Json.toJson(userChecker));
			} else {
				user.updateUser(objectNode);
				return ok(Json.toJson(user));
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete user
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			Users user = LoginController.getUser();
			if(user.admin) {
				Users userChecker = Users.find.byId(id);
				
				for(Bids bid : userChecker.bids) {
					bid.delete();
				}
				
				for(Wishlists wish : userChecker.wishlists) {
					wish.delete();
				}
				
				for(Reviews review : userChecker.reviews) {
					review.delete();
				}
				
				for(Sales sale : userChecker.sales) {
					for(Pictures picture : sale.product.pictures) {
						picture.delete();
					}
					
					for(ProductCategory category : sale.product.productcategory) {
						category.delete();
					}
					
					sale.product.delete();
					sale.delete();
				}
				
				userChecker.address.delete();
				userChecker.delete();
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
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
			Users userChecker = LoginController.getUser();	
		
			JsonNode objectNode = request().body().asJson();
			userChecker.updatePassword(objectNode);
			userChecker.deleteAuthToken();
			
			return ok(Json.toJson(""));
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
				      		+ "<a href=https://auction-house-frontend.herokuapp.com/new-password?token="+token+">Password Reset Link</a>"
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
	
	//Validate upload
	@Security.Authenticated(Secured.class)
	public Result validate(String name, String type, Integer size) {
		try {
			Users userChecker = LoginController.getUser();
			S3Signature s3 = new S3Signature(name, type, size);
			
           	return ok(s3.getS3EmberNode());
		}catch(Exception e) {
			return badRequest();
		}
	}
    
}