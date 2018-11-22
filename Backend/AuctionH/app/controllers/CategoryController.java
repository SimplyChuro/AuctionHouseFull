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
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CategoryController extends Controller{
	
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
	
	//Get one category	
	public Result get(Long id) {
		try {
			Category category = Category.find.byId(id);
			return ok(Json.toJson(category));
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
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
		} catch(Exception e) {
			return badRequest();
		}
	}
}