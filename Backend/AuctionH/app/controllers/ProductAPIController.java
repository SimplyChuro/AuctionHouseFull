package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Product;
import models.UserBid;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductAPIController extends Controller{
	
	//API GET all products and product pictures,product category list
	public Result productList() {
		try {
			List<Product> products = Product.find.all();
			return ok(Json.toJson(products));
		}catch(Exception e){
			return notFound(views.html._404.render());
		}
	}
	
	//API GET top 5 bidders of a product
	public Result productBidderList(Long id) {
		try {
			List<UserBid> bidList = UserBid.find.query().where().conjunction()
					.eq("product_bid_reference_id", id )
					.endJunction()
					.orderBy("userBidAmount dsc")
			        .setFirstRow(0)
			        .setMaxRows(5)
			        .findList();
			
			ArrayNode userProductBidArray = Json.newArray();
			for(UserBid bid : bidList) {
				userProductBidArray.add(bid.toJsonProduct());
			}
			
			return ok(Json.toJson(userProductBidArray));
		}catch(Exception e){
			return notFound(views.html._404.render());
		}
	}
	
}
