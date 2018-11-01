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
	public Result userList() {
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
	public Result verifyEmail(Long id) {
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
	
	
	
	//Create bid
	public Result createBid() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			try {
				Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				return badRequest();
			}catch(Exception e) {
				Bids bid = Json.fromJson(jsonNode, Bids.class);
				bid.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
				bid.product = Products.find.byId(jsonNode.findPath("product_id").asLong());
				bid.save();
				return ok();	
			}
		}catch(Exception e){
			return badRequest();
		}
	}	
	S
	//Update bid		
	public Result updateBid() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.update();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}		
		
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	//Delete bid		
	public Result deleteBid() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.delete();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}	
	
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	
	//Create wishlist
	public Result createWishlist() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				return badRequest();
			}catch(Exception e) {
				Wishlists wishlistItem = Json.fromJson(jsonNode, Wishlists.class);
				wishlistItem.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
				wishlistItem.product = Products.find.byId(jsonNode.findPath("product_id").asLong());
				wishlistItem.save();
				return ok();
			}
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	//Update wishlist
	public Result updateWishlist() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.update();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}
			
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Delete wishlist
	public Result deleteWishlist() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			try {
				Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findList().get(0);
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.delete();
				return ok();
			}catch(Exception e) {
				return badRequest();
			}
			
		}catch(Exception e){
			return badRequest();
		}
	}
		
	
	//Create Sale
	////////////////////////////////////////////
	///////////////NOT FINISHED/////////////////
	////////////////////////////////////////////
	////////////////////////////////////////////
	public Result createSale() {
		try {
			
			JsonNode jsonNode = request().body().asJson();	
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
			saleItem.product.save();
			
			JsonNode arrayNode = jsonNode.get("").readTree().get("objects");
//			if (arrayNode.isArray()) {
//			    for (final JsonNode objNode : arrNode) {
//			        System.out.println(objNode);
//			    }
//			}
			
//			for (int i = 0; i < catIds.size(); i++) {
//				ProductCategory categoryConnection = new ProductCategory();
//				categoryConnection.product = saleItem.product;
//				categoryConnection.category = Category.find.byId(jsonNode.findPath("product_category_category_id").asLong());
//				categoryConnection.save();
//			}
			
			for(Pictures picture : saleItem.product.pictures) {
				picture.product = saleItem.product;
				picture.save();
			}
			
			saleItem.save();
			
			return ok(Json.toJson(saleItem.product.category));	
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Update Sale
	public Result updateSale() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.update();
			saleItem.update();
			return ok();
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Delete Sale
	public Result deleteSale() {
		try {
			JsonNode jsonNode = request().body().asJson();	
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.delete();
			saleItem.delete();
			return ok();
		}catch(Exception e){
			return badRequest();
		}
	}
}
