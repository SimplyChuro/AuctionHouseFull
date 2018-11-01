package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Products;
import models.Sales;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Bids;
import models.Users;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class ProductController extends Controller {
	
	//get products
	public Result productList() {
		try {
			List<Products> products = Products.find.all();
			return ok(Json.toJson(products));
		}catch(Exception e){
			return notFound();
		}
	}
	
	//Create product
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			product.save();
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update product
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			product.update();
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete product
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			product.delete();
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get main category list
	public Result categoryList() {
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
	public Result subCategoryList(Long id) {
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
	
	//create category
	public Result createCategory() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.save();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get top 5 bidder amounts  NOT FINISHED
	public Result productBids(Long id) {
		try {
			List<Bids> bidList = Bids.find.query().where().conjunction()
					.eq("product_id", id)
					.endJunction()
					.orderBy("bidAmount desc")
			        .setMaxRows(5)
			        .findList();
			
			return ok(Json.toJson(bidList));
		}catch(Exception e){
			return notFound();
		}
	}
}
