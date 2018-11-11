package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Products;
import models.Users;
import models.Wishlists;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class WishlistController extends Controller{

	//Create wishlist
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();
			if(wishlistChecker != null) {
				return badRequest();
			} else {
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
				wishlistChecker.product = Products.find.byId(jsonNode.findPath("product_id").asLong());
				wishlistChecker.save();
				return ok();
			}
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	//Update wishlist
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();
			if(wishlistChecker != null) {
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.update();
				return ok();
			} else {
				return badRequest();
			}
			
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Delete wishlist
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();			
			if(wishlistChecker != null) {
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.delete();
				return ok();
			} else {
				return badRequest();
			}
			
		}catch(Exception e){
			return badRequest();
		}
	}

}
