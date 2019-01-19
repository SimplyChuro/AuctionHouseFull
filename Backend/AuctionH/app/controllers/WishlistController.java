package controllers;

import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Products;
import models.Sales;
import models.Users;
import models.Wishlists;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class WishlistController extends Controller{

	private HttpExecutionContext httpExecutionContext;
	private Wishlists wishlistChecker;
	private Users user;
	private JsonNode objectNode;
	
	@Inject
    public WishlistController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get one wishlist
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin) {
					wishlistChecker = Wishlists.find.byId(id);
					return ok(Json.toJson(wishlistChecker));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get all wishlist items
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					List<Wishlists> wishlist = user.wishlists;
					return ok(Json.toJson(wishlist));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Create wishlist
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					objectNode = request().body().asJson().get("wishlist");
					wishlistChecker = Wishlists.find.query().where().conjunction()
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
		}, httpExecutionContext.current());
	}	
	
	//Update wishlist
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					wishlistChecker = Wishlists.find.byId(id);
					objectNode = request().body().asJson().get("wishlist");
					
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
		}, httpExecutionContext.current());
	}
	
	//Delete wishlist
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					wishlistChecker = Wishlists.find.byId(id);
					
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
		}, httpExecutionContext.current());
	}

}