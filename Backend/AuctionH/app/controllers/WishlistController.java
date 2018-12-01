package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Products;
import models.Sales;
import models.Users;
import models.Wishlists;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class WishlistController extends Controller{

	//Get w
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			Wishlists wishlist = Wishlists.find.query().where().conjunction()
					.eq("user_id", user.id)
					.eq("product_id", id)
					.endJunction()
					.findUnique();
			
			return ok(Json.toJson(wishlist));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get  wishlist
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			List<Wishlists> wishlist = user.wishlists;
			return ok(Json.toJson(wishlist));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create wishlist
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode objectNode = request().body().asJson().get("wishlist");
			Users user = LoginController.getUser();
			
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction()
					.eq("user_id", user.id)
					.eq("product_id", objectNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(wishlistChecker != null) {
				return badRequest();
			} else {
				wishlistChecker = Json.fromJson(objectNode, Wishlists.class);
				wishlistChecker.createWishlist(user, objectNode);
				return ok(Json.toJson(wishlistChecker));
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Update wishlist
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction()
					.eq("user_id", jsonNode.findPath("user_id").asLong())
					.eq("product_id", jsonNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(wishlistChecker != null) {
				wishlistChecker = Json.fromJson(jsonNode, Wishlists.class);
				wishlistChecker.update();
				return ok();
			} else {
				return badRequest();
			}
			
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete wishlist
	@Security.Authenticated(Secured.class)	
	public Result delete(Long id) {
		try {
			Users user = LoginController.getUser();
			Wishlists wishlistChecker = Wishlists.find.query().where().conjunction()
					.eq("user_id", user.id)
					.eq("id", id)
					.endJunction()
					.findUnique();	
		
			if(wishlistChecker != null) {
				wishlistChecker.delete();
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
			
		} catch(Exception e) {
			return badRequest();
		}
	}

}