package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.Sales;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class SaleController extends Controller {
	
	
//Create Sale
	////////////////////////////////////////////
	///////////////NOT FINISHED/////////////////
	////////////////////////////////////////////
	////////////////////////////////////////////
	public Result create() {
		try {
//				
//			JsonNode jsonNode = request().body().asJson();	
//			
//			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
//			saleItem.user = Users.find.byId(jsonNode.findPath("user_id").asLong());
//			saleItem.product.save();
//			
//			JsonNode arrayNode = jsonNode.get("").readTree().get("objects");
//			if (arrayNode.isArray()) {
//			    for (final JsonNode objNode : arrNode) {
//			        System.out.println(objNode);
//			    }
//			}
//			
//			for (int i = 0; i < catIds.size(); i++) {
//				ProductCategory categoryConnection = new ProductCategory();
//				categoryConnection.product = saleItem.product;
//				categoryConnection.category = Category.find.byId(jsonNode.findPath("product_category_category_id").asLong());
//				categoryConnection.save();
//			}
//			
//			for(Pictures picture : saleItem.product.pictures) {
//				picture.product = saleItem.product;
//				picture.save();
//			}
//			
//			saleItem.save();

			return ok();	
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Update Sale
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.update();
			saleItem.update();
			return ok();
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Delete Sale
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();	
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.delete();
			saleItem.delete();
			return ok();
		}catch(Exception e){
			return badRequest();
		}
	}
		
}
