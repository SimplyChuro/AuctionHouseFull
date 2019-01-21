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

	//Get one wishlist
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				Wishlists wishlist = Wishlists.find.byId(id);
				return ok(Json.toJson(wishlist));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get all wishlist items
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				List<Wishlists> wishlist = user.wishlists;
				return ok(Json.toJson(wishlist));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create wishlist
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				JsonNode objectNode = request().body().asJson().get("wishlist");
				Wishlists wishlistChecker = Wishlists.find.query().where().conjunction()
						.eq("user_id", user.id)
						.eq("product_id", objectNode.findPath("product_id").asLong())
						.endJunction()
						.findUnique();
				
				if(wishlistChecker != null) {
					return badRequest();
				} else {
					wishlistChecker = Json.fromJson(objectNode, Wishlists.class);
					if(wishlistChecker.createWishlist(user, objectNode)) {
						return ok(Json.toJson(wishlistChecker));
					} else {
						return badRequest();
					}
				}
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Update wishlist
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				Wishlists wishlistChecker = Wishlists.find.byId(id);
				JsonNode objectNode = request().body().asJson().get("wishlist");
				
				if(wishlistChecker != null) {
					wishlistChecker = Json.fromJson(objectNode, Wishlists.class);
					wishlistChecker.update();
					return ok(Json.toJson(wishlistChecker));
				} else {
					return badRequest();
				}
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
			if(!user.admin && user.active) {
				Wishlists wishlistChecker = Wishlists.find.byId(id);
				
				if(wishlistChecker != null) {
					wishlistChecker.delete();
					return ok(Json.toJson(""));
				} else {
					return badRequest();
				}
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}

}