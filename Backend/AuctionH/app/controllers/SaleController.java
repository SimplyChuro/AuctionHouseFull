package controllers;

import java.io.File;
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
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class SaleController extends Controller {
	
	//Get sale
	@Security.Authenticated(Secured.class)
	public Result get(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				Sales sale = Sales.find.byId(id);
				return ok(Json.toJson(sale));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get sales
	@Security.Authenticated(Secured.class)
	public Result getAll() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				List<Sales> sales = user.sales;
				return ok(Json.toJson(sales));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Create sale
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				JsonNode objectNode = request().body().asJson().get("sale");	
				JsonNode productNode = objectNode.get("product");
				Long cat_id = productNode.get("category_id").asLong();
				Products product = Json.fromJson(productNode, Products.class);
				product.save();
				
				for(Pictures picture : product.pictures) {
					picture.product = product;
					picture.save();
				}
				
				Sales saleItem = Json.fromJson(objectNode, Sales.class);
				saleItem.user = user;
				saleItem.product = product;
				
				Category childCategory = Category.find.byId(cat_id);
				Category parentCategory = Category.find.byId(childCategory.parent_id);
				
				ProductCategory categoryConnectionChild = new ProductCategory();
				categoryConnectionChild.product = saleItem.product;
				categoryConnectionChild.category = childCategory;
				categoryConnectionChild.save();
				
				ProductCategory categoryConnectionParent = new ProductCategory();
				categoryConnectionParent.product = saleItem.product;
				categoryConnectionParent.category = parentCategory;
				categoryConnectionParent.save();
			
	
				saleItem.save();
	
				return ok(Json.toJson(saleItem));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update sale
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				JsonNode jsonNode = request().body().asJson();
				
				Sales saleItem = Json.fromJson(jsonNode, Sales.class);
				saleItem.product.update();
				saleItem.update();
				
				return ok(Json.toJson(saleItem));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete sale
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			Users user = LoginController.getUser();
			if(!user.admin) {
				Sales saleItem = Sales.find.byId(id);
				for(Pictures picture : saleItem.product.pictures) {
					picture.delete();
				}
				saleItem.product.delete();
				saleItem.delete();
				
				return ok(Json.toJson(""));
			} else {
				return badRequest();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
		
}