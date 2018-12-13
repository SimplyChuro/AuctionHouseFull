package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Products;
import models.Reviews;
import models.Sales;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Bids;
import models.Users;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class ProductController extends Controller {
	
	//Get product
	public Result get(Long id) {
		try {
			Products product = Products.find.byId(id);
				
			JsonNode filteredProduct = Json.toJson(product);
			
        	for(JsonNode bidListNode : filteredProduct.get("bids")) {
        		ObjectNode currentUser = (ObjectNode)bidListNode.get("user");
        		currentUser.remove("email");
        		currentUser.remove("emailVerified");
        		currentUser.remove("gender");
        		currentUser.remove("dateOfBirth");
        		currentUser.remove("phoneNumber");
        		currentUser.remove("address");
        	}
        	
	      
        	for(JsonNode reviewNode : filteredProduct.get("reviews")) {
        		ObjectNode currentUser = (ObjectNode) reviewNode.get("user");
        		currentUser.remove("email");
        		currentUser.remove("emailVerified");
        		currentUser.remove("gender");
        		currentUser.remove("dateOfBirth");
        		currentUser.remove("phoneNumber");
        		currentUser.remove("phoneVerified");
        		currentUser.remove("address");
        	}
	       
			return ok(filteredProduct);
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//Get products
	public Result getAll(Integer category, String name, String featured, String status, String rating) {
		try {
			List<Products> products = new ArrayList<>();
			Boolean checker = false;
			Date currentDate = new Date();
			
			if (name != null && !name.isEmpty()) {
				if (!(category == 0)) {
					
					List<ProductCategory> productCategories = ProductCategory.find.query().where()
							.conjunction()
							.eq("category_id", category)
							.endJunction()
							.findList();
					
					for(ProductCategory cat : productCategories) {
						if(cat.product.name.equals(name) && cat.product.expireDate.after(currentDate)) {
							products.add(cat.product);
						}
					}
					
				} else {
					
					products = Products.find.query().where()
							.conjunction()
							.eq("name", name)
							.ge("expireDate", currentDate)
							.endJunction()
							.orderBy("name asc")
							.findList();
				}
				
			} else if (!(category == 0)) {
				
				List<ProductCategory> productCategories = ProductCategory.find.query().where()
						.conjunction()
						.eq("category_id", category)
						.endJunction()
						.findList();
				
				for(ProductCategory cat : productCategories) {
					if(cat.product.expireDate.after(currentDate)) {
						products.add(cat.product);
					}
				}
				
			} else if (featured != null && !featured.isEmpty()) {
				
				products = Products.find.query().where()
						.conjunction()
						.eq("featured", true)
						.ge("expireDate", currentDate)
						.endJunction()
						.orderBy("name asc")
						.findList();
				
			} else if (status != null && !status.isEmpty()) {
				if(status.equals("new")) {
					
					products = Products.find.query().where()
							.conjunction()
							.ge("expireDate", currentDate)
							.endJunction()
							.orderBy("publishDate asc")
							.findList();
					
				} else if(status.equals("ending")) {
					products = Products.find.query().where()
							.conjunction()
							.ge("expireDate", currentDate)
							.endJunction()
							.orderBy("expireDate asc")
							.findList();
				}
			} else {
				products = Products.find.all();
			}
			
			
			
			
			
			JsonNode filteredProducts = Json.toJson(products);
			
	        for (JsonNode productListNode : filteredProducts) {
	        	for(JsonNode bidListNode : productListNode.get("bids")) {
	        		ObjectNode currentUser = (ObjectNode)bidListNode.get("user");
	        		currentUser.remove("email");
	        		currentUser.remove("emailVerified");
	        		currentUser.remove("gender");
	        		currentUser.remove("dateOfBirth");
	        		currentUser.remove("phoneNumber");
	        		currentUser.remove("phoneVerified");
	        		currentUser.remove("address");
	        	}
	        }
	        
	        for (JsonNode productListNode : filteredProducts) {
	        	for(JsonNode reviewNode : productListNode.get("reviews")) {
	        		ObjectNode currentUser = (ObjectNode) reviewNode.get("user");
	        		currentUser.remove("email");
	        		currentUser.remove("emailVerified");
	        		currentUser.remove("gender");
	        		currentUser.remove("dateOfBirth");
	        		currentUser.remove("phoneNumber");
	        		currentUser.remove("phoneVerified");
	        		currentUser.remove("address");
	        	}
	        }
			
			return ok(filteredProducts);
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//Create product
	@Security.Authenticated(Secured.class)
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			product.save();
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.save();
			}
			
			return ok(Json.toJson(product));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update product
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			product.update();
			
			for(Pictures picture : product.pictures) {
				picture.product = product;
				picture.update();
			}
			
			return ok(Json.toJson(product));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete product
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
	
			Products product = Json.fromJson(jsonNode, Products.class);
			for(Pictures picture : product.pictures) {
				picture.delete();
			}
			
			product.delete();
					
			return ok(Json.toJson(""));
		} catch(Exception e) {
			return badRequest();
		}
	}
	
}