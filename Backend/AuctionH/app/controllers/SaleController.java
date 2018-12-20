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
	
	//Create sale
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {		
			JsonNode jsonNode = request().body().asJson().get("sale");	
			JsonNode productNode = jsonNode.get("product");
			Long cat_id = productNode.get("category_id").asLong();
			Products product = Json.fromJson(productNode, Products.class);
			product.save();
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.save();
			}
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.user = LoginController.getUser();
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
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update sale
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.update();
			saleItem.update();
			
			return ok(Json.toJson(saleItem));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete sale
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();	
			
			Sales saleItem = Json.fromJson(jsonNode, Sales.class);
			saleItem.product.delete();
			saleItem.delete();
			
			return ok(Json.toJson(""));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Upload pictures -- not finished
		@Security.Authenticated(Secured.class)
		public Result upload(Long id) {
			try {
				Http.MultipartFormData<File> body = request().body().asMultipartFormData();
			    Http.MultipartFormData.FilePart<File> pictureFile = body.getFile("picture");
			    
			    if (pictureFile != null) {
			        String fileName = pictureFile.getFilename();
			        String contentType = pictureFile.getContentType();
			        File file = pictureFile.getFile();
			        Users userChecker = LoginController.getUser();
					if(userChecker.admin) {
						
						Products product = Products.find.byId(id);
						for(Pictures picture : product.pictures) {
							picture.product = product;
							picture.update();
						}
								
						return ok(Json.toJson(product));
					} else {
						Products product = Products.find.byId(id);
						for(Pictures picture : product.pictures) {
							picture.product = product;
							picture.update();
						}
								
						return ok(Json.toJson(product));
					}
			    } else {
			        return badRequest();
			    }
			} catch(Exception e) {
				return badRequest();
			}
		}
		
}