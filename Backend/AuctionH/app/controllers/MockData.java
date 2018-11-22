package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Products;

public class MockData {
	
	public void create() {
		
		//Base objects		
		Category category, childCategory;
		Products product;
		Pictures picture;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date start, end;
		
		//Category data
		String parentCategoryName [] = {"Clothes", "Accessories", "Shoes", "Jewlery", "Electronics", "Mobile", "Computer"};
		String childCategoryName [][] = {
				{"Men", "Women", "Boys", "Girls"}, 
				{"Men", "Women", "Children"}, 
				{"Formal", "Traditional", "Summer", "Winter", "Fall", "Spring"},
				{"Rings", "Earrings", "Necklace", "Bracelet", "Miscellaneous"},  
				{"Appliances", "Parts", "Tools", "Accessories", "Miscellaneous"},
				{"Android", "iOS", "Parts", "Accessories"},
				{"Hardware", "Monitors", "Laptops", "Accessories"}		
		};
		
		//Product data
		String name [] = {
				"Fancy Spring Sneakers", 
				"Plain Red T-shirt", 
				"Plain Jean Pants",
				"ACER Laptop", 
				"Samsung Galaxy S8", 
				"Seiko Watch", 
				"Wool Sweater", 
				"Logitech G502", 
				"Used Canon Camera", 
				"Warm Winter Jacket",
				"Leather Boots",
				"iPhone X",
				"Flat Screen 4K TV",
				"LucidSound Headphones",
				"Blue Jeans"
		};
		
		String startDate [] = {
				"2018-05-17", 
				"2018-11-17", 
				"2018-06-17", 
				"2017-11-11", 
				"2018-01-17", 
				"2018-01-17", 
				"2018-01-17", 
				"2018-01-11", 
				"2018-01-11", 
				"2018-01-11",
				"2018-01-17", 
				"2018-01-21", 
				"2018-01-14", 
				"2018-01-11",
				"2018-01-04"
		};
		
		String endDate [] = {
				"2019-01-01", 
				"2019-09-23", 
				"2018-12-05", 
				"2019-06-11", 
				"2019-05-17", 
				"2019-05-17", 
				"2019-05-17", 
				"2019-02-17", 
				"2019-02-21", 
				"2019-04-11",
				"2019-04-21",
				"2019-04-16",
				"2019-04-12",
				"2019-04-13",
				"2019-04-14"
				
		};
		
		Double mainBid [] = {
				93.99, 
				12.39, 
				23.49,
				679.59, 
				430.49, 
				240.20, 
				25.40, 
				49.99, 
				349.99, 
				129.99,
				69.99,
				999.99,
				1249.99,
				149.99,
				29.99
				
		};
		
		Double startingPrice [] = {
				93.99, 
				12.39, 
				23.49,
				679.59, 
				430.49, 
				240.20, 
				25.40, 
				49.99, 
				349.99, 
				129.99,
				69.99,
				999.99,
				1249.99,
				149.99,
				29.99
		};
		
		String status [] = {
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active",
				"Active", 
				"Active", 
				"Active", 
				"Active", 
				"Active"
		};
		
		String color [] = {
				"Blue", 
				"Red", 
				"Blue", 
				"Black", 
				"Black", 
				"Black", 
				"White", 
				"Black", 
				"Black",
				"Black",
				"Black",
				"Gold",
				"White",
				"Black",
				"Blue"
		};
		
		String size [] = {
				"Medium", 
				"Large", 
				"Small", 
				"Small", 
				"Large", 
				"Meduim", 
				"Large", 
				"Medium", 
				"Medium", 
				"Medium",
				"Small",
				"Medium",
				"Large",
				"Medium",
				"Extra Large"
		};
		
		String description [] = {
				"Stylish Blue Sneakers, previously used for only 1 motnhs, in good shape. Like brand new, no issues anywhere.", 
				"New T-shirts for auction, fully cotton about 99.5%. Never used or worn. They come in all shapes and sizes.", 
				"Black goth pants signed by a Norwegian Metal Band.", 
				"Brand new ACER Gaming Laptop for auction. With an i5 8600k processor, 16 gigabytes of DDR4 2600hz ram, 1 TB of SSD storage and a Nvidia GTX 1070.", 
				"New Samsung Galaxy S8 phones, they come in all shapes and sizes!",
				"Strong Reliable Comfortable and Stylish Seiko Watch. It has a nice black finish. Electronic battery can last for five years without change. Used for about 2 years.",		
				"Hand made wool sweater, crafted from top quality wool. Soft to the touch, never before used.",
				"The G502 is a professional gaming mouse made by Logitech for all gamers. I bought it 2 months ago but I never got to use it so now I am selling it for cheap.",
				"Used Canon Camera, I've been using it for multiple years and it has served me quite well. It's a plain well kept DSLR camera.",
				"NEW Winter Jackets, imported from Europe. Extremely well made and extremely warm. They come in all shapes and sizes.",
				"Slick Leather Boots, never worn extremely long and very comfortable latex.",
				"Brand New iPhone 10. It has a golden outer finish and looks extremely fancy. It comes with all the accessories.",
				"New 65 inch 4K television, it has a very slick design and sharp edges. The quality of the picture is extremely perstine.",
				"Used LucidSound headphones, I've been using them for about 2 months and I have no usage for them anymore. They are wireless and have the option to become wired with a 3.5 jack pin",
				"Vintage Blue Jeans, in a bad shape. These jeans have been worn and torn essentially they are a pair of vintage Jeans that have been damaged over time"
		};
		
		String baseURL = "https://muffin.ml/maki/churo-images/products/";
		String baseJPG = ".jpg";
		
		//Product picture data
		String pictureUrl [][] = {
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}, 
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour"},
				{"pictureOne", "pictureTwo", "pictureThree"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour"},
				{"pictureOne", "pictureTwo", "pictureThree"},
				{"pictureOne", "pictureTwo", "pictureThree"},
				{"pictureOne", "pictureTwo", "pictureThree", "pictureFour", "pictureFive"}
		};
		
		
		//Product category
		String productCategory [][] = {
				{"Shoes", "Spring"}, 
				{"Clothes", "Men"}, 
				{"Clothes", "Women"}, 
				{"Computer", "Laptops"}, 
				{"Mobile", "Android"},
				{"Accessories", "Men"},
				{"Clothes", "Men"},
				{"Computer", "Accessories"},
				{"Computer", "Accessories"},
				{"Clothes", "Women"},
				{"Clothes", "Women"},
				{"Mobile", "iOS"},
				{"Electronics", "Appliances"},
				{"Computer", "Accessories"},
				{"Clothes", "Men"}
				
		};
		
		
		
		
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
			
			for(String pic : pictureUrl[i]) {
				picture = new Pictures(baseURL+(i+1)+"/"+pic+baseJPG, product);
				picture.save();
				
			}
			
			category = Category.find.query().where().conjunction()
					.eq("parent_id", null)
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