package controllers;

import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Products;
import models.Reviews;
import models.Users;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class ReviewController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private Users user;
	private JsonNode objectNode;
	private Reviews review;
	
	@Inject
    public ReviewController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get review
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					review = Reviews.find.byId(id);
					
					return ok(Json.toJson(review));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get reviews
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					List<Reviews> reviews = user.reviews;
					return ok(Json.toJson(reviews));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Create review
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					objectNode = request().body().asJson().get("review");
					review = Json.fromJson(objectNode, Reviews.class);
					review.createReview(user, objectNode);	
					
					return ok(Json.toJson(review));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}	
	
	//Update review	
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> update(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					objectNode = request().body().asJson().get("review");	
					review = Reviews.find.byId(id);
					
					if(review != null) {
						review = Json.fromJson(objectNode, Reviews.class);
						review.update();
						return ok(Json.toJson(review));
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
	
	//Delete review	
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {	
					review = Reviews.find.byId(id);
					
					if(review != null) {
						review.delete();
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