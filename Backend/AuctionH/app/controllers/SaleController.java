package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Sales;
import models.Users;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class SaleController extends Controller {
	
	//Get sale not in usage
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LogController.getUser();
			Sales sale = Sales.find.query().where().conjunction().eq("user_id", user.id).eq("product_id", id).endJunction().findUnique();
			return ok(Json.toJson(sale));
		}catch(Exception e){
			return badRequest();
		}
	}
	
	//Get sales
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LogController.getUser();
			List<Sales> sales = user.sales;
			return ok(Json.toJson(sales));
		}catch(Exception e){
			return badRequest();
		}
	}
	
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
