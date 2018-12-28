package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Reviews;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class ReviewController extends Controller {
	
	//Get review
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				Reviews review = Reviews.find.byId(id);
				
				return ok(Json.toJson(review));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get reviews
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				List<Reviews> reviews = user.reviews;
				return ok(Json.toJson(reviews));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create review
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				JsonNode objectNode = request().body().asJson().get("review");
				Reviews review = Json.fromJson(objectNode, Reviews.class);
				review.createReview(user, objectNode);	
				
				return ok(Json.toJson(review));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Update review	
	@Security.Authenticated(Secured.class)	
	public Result update(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				JsonNode objectNode = request().body().asJson().get("review");	
				Reviews review = Reviews.find.byId(id);
				
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
	}	
	
	//Delete review	
	@Security.Authenticated(Secured.class)	
	public Result delete(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {	
				Reviews review = Reviews.find.byId(id);
				
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
	}	
}