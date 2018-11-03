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
	public Result update() {
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
	public Result delete() {
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

}
