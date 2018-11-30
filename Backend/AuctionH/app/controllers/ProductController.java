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
	
	//get products
	public Result getAll() {
		try {
			List<Products> products = Products.find.all();
			
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
			
			return ok(filteredProducts);
		} catch(Exception e) {
			return notFound();
		}
	}
	
	//get product
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
	        
			return ok(filteredProduct);
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
			
			return ok();
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
			
			return ok();
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
					
			return ok();
		} catch(Exception e) {
			return badRequest();
		}
	}
	
}