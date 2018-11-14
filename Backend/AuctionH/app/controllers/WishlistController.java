package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Products;
import models.Sales;
import models.Users;
import models.Wishlists;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class WishlistController extends Controller{

	//Get sale not in usage
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LogController.getUser();
			Wishlists wishlist = Wishlists.find.query().where().conjunction().eq("user_id", user.id).eq("product_id", id).endJunction().findUnique();
			return ok(Json.toJson(wishlist));
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Get sales
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LogController.getUser();
			List<Wishlists> wishlist = user.wishlists;
			return ok(Json.toJson(wishlist));
		}catch(Exception e){
			return badRequest();
		}
	}
	
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
