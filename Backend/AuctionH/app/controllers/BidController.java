package controllers;


import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Bids;
import models.Products;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class BidController extends Controller {
	
	//Get bid not in usage right now
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			Bids bid = Bids.find.query().where().conjunction()
					.eq("user_id", user.id)
					.eq("product_id", id)
					.endJunction()
					.findUnique();
			
			return ok(Json.toJson(bid));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get bids
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			List<Bids> bids = user.bids;
			return ok(Json.toJson(bids));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create or Update bid
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode objectNode = request().body().asJson().get("bid");
			Users user = LoginController.getUser();
			
			Bids bidChecker = Json.fromJson(objectNode, Bids.class);
			if(bidChecker.createBid(user, objectNode)) {
				return ok(Json.toJson(bidChecker));
			} else {
				return badRequest();
			}
				
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Update bid	
	@Security.Authenticated(Secured.class)	
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
				
			Bids bidChecker = Bids.find.query().where().conjunction()
					.eq("user_id", jsonNode.findPath("user_id").asLong())
					.eq("product_id", jsonNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(bidChecker != null) {
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.update();
				return ok();
			} else {
				return badRequest();
			}		
		} catch(Exception e) {
			return badRequest();
		}
	}	
	
	//Delete bid	
	@Security.Authenticated(Secured.class)	
	public Result delete(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Bids bidChecker = Bids.find.query().where().conjunction()
					.eq("user_id", jsonNode.findPath("user_id").asLong())
					.eq("product_id", jsonNode.findPath("product_id").asLong())
					.endJunction()
					.findUnique();
			
			if(bidChecker != null) {
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.delete();
				return ok();
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
}