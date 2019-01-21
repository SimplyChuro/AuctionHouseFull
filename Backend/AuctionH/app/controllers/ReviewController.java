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
			Reviews review = Reviews.find.query().where().conjunction()
					.eq("user_id", user.id)
					.eq("product_id", id)
					.endJunction()
					.findUnique();
			
			return ok(Json.toJson(review));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get reviews
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			List<Reviews> review = user.reviews;
			
			return ok(Json.toJson(review));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create review
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode objectNode = request().body().asJson().get("review");
			Users user = LoginController.getUser();
			
			Reviews review = Json.fromJson(objectNode, Reviews.class);
			review.createReview(user, objectNode);	
			
			return ok(Json.toJson(review));
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Update review	
	@Security.Authenticated(Secured.class)	
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
				
			Reviews review = Reviews.find.query().where().conjunction()
					.eq("user_id", jsonNode.findPath("user_id").asLong())
					.eq("product_id", jsonNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(review != null) {
				review = Json.fromJson(jsonNode, Reviews.class);
				review.update();
				return ok(Json.toJson(review));
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
			JsonNode jsonNode = request().body().asJson();
			
			
			Reviews review = Reviews.find.query().where().conjunction()
					.eq("user_id", jsonNode.findPath("user_id").asLong())
					.eq("product_id", jsonNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(review != null) {
				review = Json.fromJson(jsonNode, Reviews.class);
				review.delete();
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
}