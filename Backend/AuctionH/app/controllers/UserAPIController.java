package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.Product;
import models.UserAddressInfo;
import models.UserBid;
import models.UserExtendedPersonalInfo;
import models.UserSaleItem;
import models.UserWishlistItem;
import models.WebUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class UserAPIController extends Controller {
	
	
	//API GET all users
	public Result userList() {
		try {
			List<WebUser> users = WebUser.find.all();
			return ok(Json.toJson(users));
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	//API POST new user
	public Result userCreateFromJSON() {
		try {
			JsonNode jsonNode = request().body().asJson();
			WebUser user = new WebUser();
			
			List<WebUser> userList = WebUser.find.query().where().conjunction().eq("email", jsonNode.findPath("email").asLong()).endJunction().findList();
			
			if(userList.isEmpty()) {
				user.name = jsonNode.findPath("name").textValue();
				user.surname = jsonNode.findPath("surname").textValue();
				user.email = jsonNode.findPath("email").textValue();
				user.password = jsonNode.findPath("password").textValue();
				user.emailVerified = false;
				user.save();
				
				return ok();
			}else {
				return notFound();
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	

	//API verify Mail
	public Result userVerifyEmailFromJSON(Long id) {
		try {
			JsonNode jsonNode = request().body().asJson();
			WebUser user = WebUser.find.byId(id);
			
			List<WebUser> userList = WebUser.find.query().where().conjunction().eq("emailVerified", false).endJunction().findList();
			
			if(userList.isEmpty()) {
				user.emailVerified = true;
				user.update();
				
				return ok();
			}else {
				return notFound();
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	} 
	
	
	//API verify login, if verified send entire user info via JSON
		public Result userVerifyLogin() {
			try {
				JsonNode jsonNode = request().body().asJson();
				List<WebUser> userList = WebUser.find.query().where().conjunction().eq("email", jsonNode.findPath("email").asLong()).eq("password", jsonNode.findPath("password").asLong()).endJunction().findList();
				
				if(userList.isEmpty()) {
					return ok(Json.toJson(userList.get(0)));
				}else {
					return notFound();
				}
			}catch(Exception e){
				return notFound(views.html._404.render());
			}
		}

		
		
		//API POST for Adress, CU - Create or Update
		public Result userAdressCUFromJSON() {
			try {
				JsonNode jsonNode = request().body().asJson();
				WebUser user = WebUser.find.byId(jsonNode.findPath("id").asLong());
				if(user != null) {
					if(user.userAddressInfo != null) {
						UserAddressInfo userAddressInfo = new UserAddressInfo();
						
						userAddressInfo.setUserAddressInfo(jsonNode);
						userAddressInfo.save();
					}else {
						UserAddressInfo userAddressInfo = user.userAddressInfo;
						
						userAddressInfo.setUserAddressInfo(jsonNode);
						userAddressInfo.update();
					}
					return ok();
				}else {
					return notFound(views.html._404.render());
				}
			}catch(Exception e){
				return notFound(views.html._404.render());
			}	
		}
		
		//API POST for Extended User Info, CU - Create or Update
		public Result userExtInfoCUFromJSON() {
			try {
				JsonNode jsonNode = request().body().asJson();
				WebUser user = WebUser.find.byId(jsonNode.findPath("id").asLong());
				if(user != null) {
					if(user.userExtendedPersonalInfo != null) {
						UserExtendedPersonalInfo userExtInfo = new UserExtendedPersonalInfo();
						
						userExtInfo.setUserExtInfo(jsonNode);
						userExtInfo.save();
					}else {
						UserExtendedPersonalInfo userExtInfo = user.userExtendedPersonalInfo;
						
						userExtInfo.setUserExtInfo(jsonNode);
						userExtInfo.update();
					}
					return ok();
				}else {
					return notFound(views.html._404.render());
				}
			}catch(Exception e){
				return notFound(views.html._404.render());
			}
		}
		
		//API POST for Bids, CU - Create or Update
		public Result userBidCUFromJSON() {
			try {
				JsonNode jsonNode = request().body().asJson();
				List<UserBid> bidList = UserBid.find.query().where().conjunction().eq("web_user_bid_reference_id", jsonNode.findPath("userID").asLong()).eq("product_bid_reference_id", jsonNode.findPath("productId").asLong()).endJunction().findList();
			
				if(bidList.isEmpty()) {
					UserBid bid = bidList.get(0);
					
					bid.setUserBidItemInfo(jsonNode);
					bid.update();
					
				}else {
					WebUser user = WebUser.find.byId(jsonNode.findPath("userID").asLong());
					Product product = Product.find.byId(jsonNode.findPath("productId").asLong());
					UserBid bid = new UserBid();
					
					bid.setUserBidItemInfo(jsonNode, user, product);
					bid.save();
				}
				
				return ok();
			}catch(Exception e){
				return notFound(views.html._404.render());
			}
		}	
		
		//API POST for WishList, CU - Create or Update
		public Result userWishlistCUFromJSON() {
			try {
				JsonNode jsonNode = request().body().asJson();
				List<UserWishlistItem> wishlistItemList = UserWishlistItem.find.query().where().conjunction().eq("web_user_wishlist_reference_id", jsonNode.findPath("userID").asLong()).eq("product_wishlist_reference_id", jsonNode.findPath("productId").asLong()).endJunction().findList();
			
				if(wishlistItemList.isEmpty()) {
					UserWishlistItem wishlistItem = wishlistItemList.get(0);
					
					wishlistItem.setUserWishlistItemInfo(jsonNode);
					wishlistItem.update();
					
				}else {
					WebUser user = WebUser.find.byId(jsonNode.findPath("userID").asLong());
					Product product = Product.find.byId(jsonNode.findPath("productId").asLong());
					UserWishlistItem wishlistItem = new UserWishlistItem();
				
					wishlistItem.setUserWishlistItemInfo(jsonNode, user, product);
					wishlistItem.save();
				}
				
				return ok();
			}catch(Exception e){
				return notFound(views.html._404.render());
			}
		}	
		
		//API POST for Bids, CU - Create or Update
		public Result userSaleCUFromJSON() {
			try {
				JsonNode jsonNode = request().body().asJson();
				List<UserSaleItem> saleList = UserSaleItem.find.query().where().conjunction().eq("web_user_sale_reference_id", jsonNode.findPath("userID").asLong()).eq("product_sale_reference_id", jsonNode.findPath("productId").asLong()).endJunction().findList();
			
				if(saleList.isEmpty()) {
					UserSaleItem saleItem = saleList.get(0);
					
					saleItem.setUserSaleItemInfo(jsonNode);
					saleItem.update();
					
				}else {
					
					WebUser user = WebUser.find.byId(jsonNode.findPath("userID").asLong());
					Product product = new Product();
					product.setProductInfo(jsonNode);
					
					UserSaleItem saleItem = new UserSaleItem();
					saleItem.setUserSaleItemInfo(jsonNode, user, product);
					saleItem.save();
				}
				
				return ok();
			}catch(Exception e){
				return notFound(views.html._404.render());
			}
		}

}
