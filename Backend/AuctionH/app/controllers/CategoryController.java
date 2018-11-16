package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Category;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CategoryController extends Controller{
	
	//Get categories
	public Result categories() {
		try {
			JsonNode jsonNode = request().body().asJson();
			List<Category> categories = Category.find.all();
			return ok(Json.toJson(categories));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get one category	
	public Result category(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			Category category = Category.find.byId(id);
			
			return ok(Json.toJson(category));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//create category  
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.save();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//update category  
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.update();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//delete category 
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.delete();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}

}
