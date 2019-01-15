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
	
	//Get bid
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				Bids bid = Bids.find.byId(id);
				
				return ok(Json.toJson(bid));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get bids
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				List<Bids> bids = user.bids;
				return ok(Json.toJson(bids));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create bid
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				JsonNode objectNode = request().body().asJson().get("bid");
				Bids bidChecker = Json.fromJson(objectNode, Bids.class);
				if(bidChecker.createBid(user, objectNode)) {
					return ok(Json.toJson(bidChecker));
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
	
	//Update bid	
	@Security.Authenticated(Secured.class)	
	public Result update(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin && user.active) {
				JsonNode objectNode = request().body().asJson().get("bid");
				Bids bidChecker = Bids.find.byId(id);
				
				if(bidChecker != null) {
					bidChecker = Json.fromJson(objectNode, Bids.class);
					bidChecker.update();
					return ok(Json.toJson(bidChecker));
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
	
	//Delete bid	
	@Security.Authenticated(Secured.class)	
	public Result delete(Long id) {
		try {
			Users user = LoginController.getUser();
			
			if(!user.admin && user.active) {
				Bids bidChecker = Bids.find.byId(id);
				
				if(bidChecker != null) {
					bidChecker.delete();
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