package controllers;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class UserController extends Controller {
	
	//API GET all users  ------ Testing only
	public Result users() {
		try {
			List<Users> users = Users.find.all();
			return ok(Json.toJson(users));
		}catch(Exception e) {
			return notFound();
		}
	}
	
	//Create user
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			try {
				Users userChecker = Users.find.query().where().conjunction().eq("email", jsonNode.findPath("email").textValue()).endJunction().findList().get(0);
				return badRequest();
			}catch(Exception e) {
				Users user = Json.fromJson(jsonNode, Users.class);
				user.emailVerified = false;
				user.save();
				user.address = new Address();
				user.address.user = user;
				user.address.save();
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
				userChecker.address.update();
				userChecker.update();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Verify Login, return user
	public Result login() {
		try {
			JsonNode jsonNode = request().body().asJson();
			Users user = Users.find.query().where().conjunction().eq("email", jsonNode.findPath("email").textValue()).eq("password", jsonNode.findPath("password").textValue()).endJunction().findList().get(0);
			
			return ok(Json.toJson(user));
		}catch(Exception e){
			return notFound();
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
	
}
