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
	public Result products() {
		try {
			List<Products> products = Products.find.all();
			JsonNode productNode = Json.toJson(products);
			String json ="{\"products\":"+Json.stringify(productNode)+"}";
			return ok(Json.parse(json));
		}catch(Exception e){
			return notFound();
		}
	}
	
	//get products
	public Result product(Long id) {
		try {
			Products product = Products.find.byId(id);
			JsonNode productNode = Json.toJson(product);
			String json ="{\"product\":"+Json.stringify(productNode)+"}";
			return ok(Json.parse(json));
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
	
	//Get top 5 bidder amounts  NOT FINISHED
	public Result bids(Long id) {
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
