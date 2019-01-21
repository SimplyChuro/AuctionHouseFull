package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Address;
import models.Bids;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class AddressController extends Controller {
		
	//Get bid not in usage
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			return ok(Json.toJson(user.address));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get bids -- not finished -- needs planning
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			return ok(Json.toJson(user.address));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create or Update bid
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode objectNode = request().body().asJson().get("address");
			Users user = LoginController.getUser();
			
			Address addressChecker = user.address;
			
			if(addressChecker == null) {
				addressChecker = Json.fromJson(objectNode, Address.class);
				
				addressChecker.save();
				return ok(Json.toJson(addressChecker));
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
			JsonNode objectNode = request().body().asJson().get("address");
			Users user = LoginController.getUser();
			
			Address addressChecker = user.address;
			
			if(addressChecker != null) {
				addressChecker = Json.fromJson(objectNode, Address.class);
				
				addressChecker.update();
				return ok(Json.toJson(addressChecker));
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
			Users user = LoginController.getUser();
			
			Address addressChecker = user.address;
			
			if(addressChecker != null) {
				addressChecker = Json.fromJson(jsonNode, Address.class);
				addressChecker.delete();
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}	
}