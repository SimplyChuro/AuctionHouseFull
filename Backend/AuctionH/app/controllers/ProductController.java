package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Product;
import models.ProductCategory;
import models.ProductPicture;
import models.WebUser;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class ProductController extends Controller {
	
	@Inject
	FormFactory formFactory;
	
	//API GET
	public Result productList() {
		try {
			List<Product> products = Product.find.all();
			ArrayNode allProducts = Json.newArray();
			for(Product product: products) {
				allProducts.add(product.toJson()) ;
			}
			return ok(allProducts);
		}catch(Exception e){
			return notFound(views.html._404.render());
		}
	}
	
	//API POST
	public Result productCreateFromJSON() {
		
		JsonNode jsonNode = request().body().asJson();
		Product product = new Product();
		try {
			
			product.name = jsonNode.findPath("name").textValue();
			product.publishDate = jsonNode.findPath("publishDate").textValue();
			product.expireDate = jsonNode.findPath("expireDate").textValue();
			product.mainBid = jsonNode.findPath("mainBid").asInt();
			product.active = jsonNode.findPath("active").asBoolean();
			product.mainDescription = jsonNode.findPath("mainDescription").textValue();
			product.additionalDescription = jsonNode.findPath("additionalDescription").textValue();
			product.startingPrice = jsonNode.findPath("startingPrice").asInt();
			product.save();
			
//			product.productCategory = new ArrayList<>();
			for (JsonNode category : jsonNode.withArray("category")) {
				ProductCategory productCategory = new ProductCategory();
				productCategory.category =  category.get("category").asText();
				productCategory.parentCategory = category.get("parentcategory").asText();
				productCategory.productCategoryReference = product;
				productCategory.save();
				//product.productCategory.add(productCategory);
			}
			
//			product.productPictures = new ArrayList<>();
			for (JsonNode picture : jsonNode.withArray("pictures")) {
				ProductPicture productPicture = new ProductPicture();
				productPicture.pictureName =  picture.get("pictureName").asText();
				productPicture.pictureDirectory = picture.get("pictureDirectory").asText();
				productPicture.productPictureReference = product;
				productPicture.save();
//				product.productPictures.add(productPicture);
			}
			
		}catch(Exception e) {
			
		}
		
		return ok();
	}
	
	
	public Result productCardList() {
		try {
			List<Product> products = Product.find.all();
			return ok(views.html.productsList.render(products));
		}catch(Exception e){
			return notFound(views.html._404.render());
		}
	}
	
	public Result getOneProduct(Long id) {
		try {
			Product product = Product.find.byId(id);
	        if(product == null){
	            return notFound(views.html._404.render());
	        }else {
	        	return ok(views.html.productView.render(product));
	        }
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result createOneProduct() {
		try {
			Form<Product> productForm = formFactory.form(Product.class);
			return ok(views.html.productCreate.render(productForm));	
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result editProduct(Long id) {
		try {
			Product product = Product.find.byId(id);
	        if(product == null){
	            return notFound(views.html._404.render());
	        }else {
				Form<Product> productForm = formFactory.form(Product.class).fill(product);
	        	return ok(views.html.productEdit.render(productForm));
	        }
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
    }
	
	public Result updateProduct() {
		try {
			Product product = formFactory.form(Product.class).bindFromRequest().get();
			Product oldProduct = Product.find.byId(product.id);
			
			if(oldProduct == null) {
				return notFound(views.html._404.render());
			}else {
				oldProduct.name = product.name;
				oldProduct.publishDate = product.publishDate;
				oldProduct.expireDate = product.expireDate;
				oldProduct.mainBid = product.mainBid;
				oldProduct.active = product.active;
				oldProduct.mainDescription = product.mainDescription;
				oldProduct.additionalDescription = product.additionalDescription;
				oldProduct.startingPrice = product.startingPrice;
				oldProduct.update();
				return redirect(routes.HomeController.index());
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
    }
	
	public Result save() {
		try {
			Form<Product> productForm = formFactory.form(Product.class).bindFromRequest();
			if(productForm.hasErrors()){
	            flash("danger", "Input validation failed.");
	            return badRequest(views.html.productCreate.render(productForm));
	        }
			
			Product product = productForm.get();
			product.save();
			
			flash("success", "Product Successfully Created/Updated");
			return redirect(routes.HomeController.index());
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result deleteProduct(Long id) {
		try {
			Product product = Product.find.byId(id);
			if(product == null) {
				return notFound(views.html._404.render());
			}else {
				product.delete();
				return redirect(routes.ProductController.productCardList());
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
}
