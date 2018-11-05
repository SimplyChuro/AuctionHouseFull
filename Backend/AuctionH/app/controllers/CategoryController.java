package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Category;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CategoryController extends Controller{
	
	//Get main category list
	public Result categories() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			List<Category> categoryList = Category.find.query().where().conjunction()
					.eq("parent_id", null)
					.endJunction()
					.orderBy("name asc")
			        .findList();
			
			return ok(Json.toJson(categoryList));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get sub category list which returns all products
	public Result category(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			List<Category> categoryList = Category.find.query().where().conjunction()
					.eq("parent_id", id)
					.endJunction()
					.orderBy("name asc")
			        .findList();
			
			return ok(Json.toJson(categoryList));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//create category  testing only
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
	
	//update category  testing only
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
	
	//delete category  testing only
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
