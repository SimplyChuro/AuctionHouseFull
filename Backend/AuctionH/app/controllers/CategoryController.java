package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Products;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class CategoryController extends Controller{
	
	//Get one category	
	public Result get(Long id) {
		try {
			Category category = Category.find.byId(id);
			return ok(Json.toJson(category));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get categories
	public Result getAll() {
		try {
			List<Category> categories = Category.find.all();
			if(categories.isEmpty()) {
				MockData data = new MockData();
				data.create();
			}
			return ok(Json.toJson(categories));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create category  
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			Users userChecker = LoginController.getUser();
			JsonNode jsonNode = request().body().asJson().get("category");
			if(userChecker.admin) {
				Category category = new Category();
				if(jsonNode.findValue("parent_id") == null || jsonNode.findValue("parent_id").asText().equals("null")) {
					category.parent_id = null;
				} else {
					category.parent_id = jsonNode.findValue("parent_id").asLong();
				}
				category.name = jsonNode.findValue("name").asText();
				
				category.save();
				return ok(Json.toJson(category));
			} else {
				return forbidden();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update category  
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			Users userChecker = LoginController.getUser();
			JsonNode jsonNode = request().body().asJson().get("category");
			if(userChecker.admin) {
				Category category = Category.find.byId(id);
				if(jsonNode.findValue("parent_id") == null || jsonNode.findValue("parent_id").asText().equals("null")) {
					category.parent_id = null;
				} else {
					category.parent_id = jsonNode.findValue("parent_id").asLong();
				}
				category.name = jsonNode.findValue("name").asText();
				
				category.update();
				return ok(Json.toJson(category));
			} else {
				return forbidden();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete category 
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			Users userChecker = LoginController.getUser();
			if(userChecker.admin) {
				Category category = Category.find.byId(id);
				
				if(!(category.parent_id == null)) {
					List<Category> categories = Category.find.query().where()
							.conjunction()
							.eq("parent_id", id)
							.endJunction()
							.findList();
					
					for(Category cat : categories) {
						for(ProductCategory productCat : cat.productcategory) {
							productCat.delete();
						}
						
						cat.delete();
						
					}
				}
				
				for(ProductCategory cat : category.productcategory) {
					cat.delete();
				}
				
				category.delete();
				return ok(Json.toJson(""));
			} else {
				return forbidden();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
}