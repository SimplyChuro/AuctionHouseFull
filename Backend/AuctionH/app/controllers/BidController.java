package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Products;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class BidController extends Controller {
	
	//Create bid
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			JsonNode objectNode = jsonNode.get("bid");
			Users user = LogController.getUser();
			Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", user.id).eq("product_id", objectNode.findPath("product_id").asLong()).endJunction().findUnique();
			if(bidChecker != null) {
				return badRequest();
			}else {
				Bids bid = Json.fromJson(objectNode, Bids.class);
				bid.user = user;
				bid.product = Products.find.byId(objectNode.findPath("product_id").asLong());
				bid.product.mainBid = bid.amount;
				bid.product.update();
				bid.save();
				return ok();
			}
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	//Update bid		
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
				
			Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();
			if(bidChecker != null) {
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.update();
				return ok();
			} else {
				return badRequest();
			}		
		
		}catch(Exception e){
			return badRequest();
		}
	}	
	
	//Delete bid		
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();
			if(bidChecker != null) {
				bidChecker = Json.fromJson(jsonNode, Bids.class);
				bidChecker.delete();
				return ok();
			} else {
				return badRequest();
			}
	
		}catch(Exception e){
			return badRequest();
		}
	}	
}
