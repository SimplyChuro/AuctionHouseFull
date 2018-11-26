package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Bids;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Products;
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
			Users user = LoginController.getUser();
			Sales sale = Sales.find.query().where()
					.conjunction()
					.eq("user_id", user.id)
					.eq("product_id", id)
					.endJunction()
					.findUnique();
			
			return ok(Json.toJson(sale));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get sales
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			List<Sales> sales = user.sales;
			return ok(Json.toJson(sales));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create Sale
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {		
			JsonNode jsonNode = request().body().asJson().get("sale");	
			JsonNode productNode = jsonNode.get("product");
			List<String> categories = productNode.findValuesAsText("categories");
			Products product = Json.fromJson(productNode, Products.class);
			product.save();
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.save();
			}
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.user = LoginController.getUser();
			saleItem.product = product;

			
			for (String category : categories) {
				String categorySpliter[] = category.split("/");
				
				Category parentCategory = Category.find.query().where()
						.conjunction()
						.eq("parent_id", null)
						.eq("name", categorySpliter[0])
						.endJunction()
						.findUnique();
				
				Category childCategory = Category.find.query().where()
						.conjunction()
						.eq("parent_id", parentCategory.id)
						.eq("name", categorySpliter[1])
						.endJunction()
						.findUnique();
				
				ProductCategory categoryConnection = new ProductCategory();
				categoryConnection.product = saleItem.product;
				categoryConnection.category = childCategory;
				categoryConnection.save();
			}

			saleItem.save();

			return ok();	
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update Sale
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.update();
			saleItem.update();
			return ok();
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete Sale
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();	
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.delete();
			saleItem.delete();
			return ok();
		} catch(Exception e) {
			return badRequest();
		}
	}
		
}