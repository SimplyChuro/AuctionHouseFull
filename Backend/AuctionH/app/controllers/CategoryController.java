package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;

import models.Bids;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Products;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CategoryController extends Controller{
	
	//Get categories
	public Result getAll() {
		try {
			List<Category> categories = Category.find.all();
			if(categories.isEmpty()) {
				createMockData();
			}
			return ok(Json.toJson(categories));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//Get one category	
	public Result get(Long id) {
		try {
			Category category = Category.find.byId(id);
			return ok(Json.toJson(category));
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//create category  
	public Result create() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.save();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//update category  
	public Result update() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.update();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}
	
	//delete category 
	public Result delete() {
		try {
			JsonNode jsonNode = request().body().asJson();
			
			Category category = Json.fromJson(jsonNode, Category.class);
			category.delete();
	
			return ok();
		}catch(Exception e) {
			return badRequest();
		}
	}

	public void createMockData() {
		
		//Base objects		
		Category category, childCategory;
		Products product;
		Pictures picture;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date start, end;
		
		//Category data
		String parentCategoryName [] = {"Clothes", "Accessories", "Jewlery", "Shoes", "Electronics", "Mobile", "Computer"};
		String childCategoryName [][] = {
				{"Men", "Women", "Boys", "Girls"}, 
				{"Men", "Women", "Children"}, 
				{"Rings", "Earrings", "Necklace", "Bracelet", "Miscellaneous"}, 
				{"Formal", "Traditional", "Summer", "Winter", "Fall", "Spring"}, 
				{"Parts", "Tools", "Accessories", "Miscellaneous"},
				{"Android", "iOS", "Parts", "Accessories"},
				{"Hardware", "Monitors", "Laptops", "Accessories"}};
		
		//Product data
		String name [] = {"Sneakers", "T-shirt", "Pants", "HP Laptop", "Samsung Galaxy S8"};
		String startDate [] = {"2018-05-17", "2018-11-17", "2018-06-17", "2017-11-11", "2018-01-17"};
		String endDate [] = {"2019-01-01", "2019-09-23", "2018-11-05", "2019-06-11", "2018-05-17"};
		Double mainBid [] = {93.42, 12.30, 23.40, 679.50, 430.49};
		String status [] = {"Active", "Active", "Sold", "Sold", "Removed"};
		String color [] = {"Blue", "Red", "Blue", "White", "Black"};
		String size [] = {"Medium", "Large", "Small", "Small", "Large"};
		String description [] = {
				"Stylish Blue Sneakers, used for only 2 motnhs", 
				"New T-shirt for auction, fully cotton 99.5%", 
				"Black goth pants signed by a Norwegian Metal Band", 
				"Brand new HP Laptop for auction", 
				"New Samsung Galaxy S8, with a black laminate finish"};
		Double startingPrice [] = {93.42, 12.30, 23.40, 679.50, 430.49};
		
		//Product picture data
		String pictureName [][] = {
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}};
		
		String pictureDirectory [][] = {
				{"public/assets/images/productOne", "public/assets/images/productOne", "public/assets/images/productOne", "public/assets/images/productOne", "public/assets/images/productOne"}, 
				{"public/assets/images/productTwo", "public/assets/images/productTwo", "public/assets/images/productTwo", "public/assets/images/productTwo", "public/assets/images/productTwo"}, 
				{"public/assets/images/productThree", "public/assets/images/productThree", "public/assets/images/productThree", "public/assets/images/productThree", "public/assets/images/productThree"},  
				{"public/assets/images/productFour", "public/assets/images/productFour", "public/assets/images/productFour", "public/assets/images/productFour", "public/assets/images/productFour"}, 
				{"public/assets/images/productFive", "public/assets/images/productFive", "public/assets/images/productFive", "public/assets/images/productFive", "public/assets/images/productFive"}};
		
		//Product category
		String productCategory [][] = {{"Shoes", "Men"}, {"Clothes", "Men"}, {"Clothes", "Women"}, {"Computer", "Laptops"}, {"Mobile", "Android"}};
		
		//Unit creation		
		for(int i = 0; i < parentCategoryName.length; i++) {
			
			category = new Category(parentCategoryName[i], null, null);
			category.save();
			
			for(String cat : childCategoryName[i]) {
				childCategory = new Category(cat, category.id, null);
				childCategory.save();
			}
		}
		
		for(int i = 0; i < name.length; i++) {
			try {
				start = format.parse(startDate[i]);
				end = format.parse(endDate[i]);
			} catch(Exception e) {
				start = new Date();
				end = new Date();
			}
			
			product = new Products(name[i], start, end, mainBid[i], status[i], color[i], size[i], description[i], startingPrice[i]);
			product.save();
			
			for(int j = 0; j < pictureName.length; j++) {
				picture = new Pictures(pictureName[i][j], pictureDirectory[i][j], product);
				picture.save();
			}
			
			category = Category.find.query().where().conjunction()
					.eq("name", productCategory[i][0])
					.endJunction()
			        .findUnique();
			
			childCategory = Category.find.query().where().conjunction()
					.eq("parent_id", category.id)
					.eq("name", productCategory[i][1])
					.endJunction()
			        .findUnique();
			
			ProductCategory productCategoryLink = new ProductCategory();
			productCategoryLink.category = childCategory;
			productCategoryLink.product = product;
			productCategoryLink.save();
		}
		
	}
}
