package controllers;

import java.io.File;
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
import models.Wishlists;
import play.mvc.Controller;
import play.mvc.Http;
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
						if(cat.product.name.equals(name) && cat.product.expireDate.after(currentDate) && cat.product.publishDate.before(currentDate)) {
							products.add(cat.product);
						}
					}
					
				} else {
					
					products = Products.find.query().where()
							.conjunction()
							.contains("name", name)
							.le("publishDate", currentDate)
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
					if(cat.product.expireDate.after(currentDate) && cat.product.publishDate.before(currentDate)) {
						products.add(cat.product);
					}
				}
				
			} else if (featured != null && !featured.isEmpty()) {
				
				products = Products.find.query().where()
						.conjunction()
						.eq("featured", true)
						.le("publishDate", currentDate)
						.ge("expireDate", currentDate)
						.endJunction()
						.orderBy("name asc")
						.findList();
				
			} else if (status != null && !status.isEmpty()) {
				if(status.equals("new")) {
					
					products = Products.find.query().where()
							.conjunction()
							.le("publishDate", currentDate)
							.ge("expireDate", currentDate)
							.endJunction()
							.orderBy("publishDate desc")
							.findList();
					
				} else if(status.equals("ending")) {
					products = Products.find.query().where()
							.conjunction()
							.le("publishDate", currentDate)
							.ge("expireDate", currentDate)
							.endJunction()
							.orderBy("expireDate asc")
							.findList();
				} else if(status.equals("all")) {
					products = Products.find.all();
				}
			} else {
				products = Products.find.query().where()
						.conjunction()
						.le("publishDate", currentDate)
						.ge("expireDate", currentDate)
						.endJunction()
						.findList();
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
			Users userChecker = LoginController.getUser();
			
			if(userChecker.admin) {
				JsonNode jsonNode = request().body().asJson().get("product");
				Long cat_id = jsonNode.get("category_id").asLong();
				
				Products product = new Products();
				product.saveProduct(jsonNode);
				
//				for(Pictures picture : product.pictures) {
//					picture.product = product;
//					picture.save();
//				}
				
				Category childCategory = Category.find.byId(cat_id);
				Category parentCategory = Category.find.byId(childCategory.parent_id);
				
				ProductCategory categoryConnectionChild = new ProductCategory();
				categoryConnectionChild.product = product;
				categoryConnectionChild.category = childCategory;
				categoryConnectionChild.save();
				
				ProductCategory categoryConnectionParent = new ProductCategory();
				categoryConnectionParent.product = product;
				categoryConnectionParent.category = parentCategory;
				categoryConnectionParent.save();
				
				return ok(Json.toJson(product));
			} else {
				return forbidden();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Update product
	@Security.Authenticated(Secured.class)
	public Result update(Long id) {
		try {
			Users userChecker = LoginController.getUser();
			
			if(userChecker.admin) {
				JsonNode jsonNode = request().body().asJson().get("product");
				
				Products product = Products.find.byId(id);
				product.updateProduct(jsonNode);
				
//				for(Pictures picture : product.pictures) {
//					picture.product = product;
//					picture.update();
//				}
				try {
					Long cat_id = jsonNode.get("category_id").asLong();
					Category childCategory = Category.find.byId(cat_id);
					Category parentCategory = Category.find.byId(childCategory.parent_id);
					
					ProductCategory categoryConnectionChild = new ProductCategory();
					categoryConnectionChild.product = product;
					categoryConnectionChild.category = childCategory;
					categoryConnectionChild.save();
					
					ProductCategory categoryConnectionParent = new ProductCategory();
					categoryConnectionParent.product = product;
					categoryConnectionParent.category = parentCategory;
					categoryConnectionParent.save();
				} catch(Exception e) {
					
				} finally {
					return ok(Json.toJson(product));	
				}
			} else {
				return forbidden();
			}
		} catch(Exception e) {
			return badRequest();
		}
	}
	
	//Delete product
	@Security.Authenticated(Secured.class)
	public Result delete(Long id) {
		try {
			Users userChecker = LoginController.getUser();
			if(userChecker.admin) {
				
				Products product = Products.find.byId(id);
				for(Pictures picture : product.pictures) {
					picture.delete();
				}
				
				for(Bids bid : product.bids) {
					bid.delete();
				}
				
				for(Wishlists wish : product.wishlists) {
					wish.delete();
				}
				
				for(Reviews review : product.reviews) {
					review.delete();
				}
				
				for(ProductCategory cat : product.productcategory) {
					cat.delete();
				}
				
				if(product.sale != null) {
					product.sale.delete();
				}
				product.delete();
						
				return ok(Json.toJson(""));
			} else {
				return forbidden();
			}
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