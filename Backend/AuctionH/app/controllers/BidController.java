package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Products;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class BidController extends Controller {
	
	//Create bid
		public Result create() {
			try {
				JsonNode jsonNode = request().body().asJson();
			
				Bids bidChecker = Bids.find.query().where().conjunction().eq("user_id", jsonNode.findPath("user_id").asLong()).eq("product_id", jsonNode.findPath("product_id").asLong()).endJunction().findUnique();
				if(bidChecker != null) {
					return badRequest();
				}else {
					Bids bid = Json.fromJson(jsonNode, Bids.class);
					bid.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
					bid.product = Products.find.byId(jsonNode.findPath("product_id").asLong());
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
