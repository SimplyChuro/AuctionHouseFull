package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	public Result getAll() {
		try {
			List<Products> products = Products.find.all();
			return ok(Json.toJson(products));
		}catch(Exception e){
			return notFound();
		}
	}
	
	//get products
	public Result get(Long id) {
		try {
			Products product = Products.find.byId(id);
			
			return ok(Json.toJson(product));
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
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.save();
			}
			
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
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.update();
			}
			
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
			for(Pictures picture : product.pictures) {
				picture.delete();
			}
			
			product.delete();
					
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get top 5 bidders
	public Result bids(Long id) {
		try {
			List<Bids> bidList = Bids.find.query().where().conjunction()
					.eq("product_id", id)
					.endJunction()
					.orderBy("bidAmount desc")
			        .setMaxRows(5)
			        .findList();
			
			List<Users> users = new ArrayList<>();
			for(Bids bid : bidList) {
				users.add(bid.getUser());
			}
			
			return ok(Json.toJson(users));
		}catch(Exception e){
			return notFound();
		}
	}
	
}
