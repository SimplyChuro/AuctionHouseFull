package controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import jdk.nashorn.internal.objects.annotations.Where;
import models.Product;
import models.ProductCategory;
import models.ProductPicture;
import models.UserAddressInfo;
import models.UserBid;
import models.UserExtendedPersonalInfo;
import models.UserSaleItem;
import models.UserWishlistItem;
import models.WebUser;

import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class UserController extends Controller {
	
	@Inject
	public FormFactory formFactory;
	
	//API GET
	public Result userList() {
		try {
			List<WebUser> users = WebUser.find.all();
			return ok(Json.toJson(users));
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	//API POST
	public Result userCreateFromJSON() {
		try {
			JsonNode jsonNode = request().body().asJson();
			WebUser user = new WebUser();
			
			user.name = jsonNode.findPath("name").textValue();
			user.surname = jsonNode.findPath("surname").textValue();
			user.email = jsonNode.findPath("email").textValue();
			user.password = jsonNode.findPath("password").textValue();
			user.emailVerified = false;
			user.save();
			
			return ok();
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result userCardList() {
		try {
			List<WebUser> users = WebUser.find.all();
			return ok(views.html.usersList.render(users));
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result getOneUser(Long id) {
		try {
			WebUser user = WebUser.find.byId(id);
	        if(user == null){
	            return notFound(views.html._404.render());
	        }else {
	        	return ok(views.html.userView.render(user));
	        }
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result createOneUser() {
		try {
			Form<WebUser> userForm = formFactory.form(WebUser.class);
			return ok(views.html.userCreate.render(userForm));
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result save() {
		try {
			Form<WebUser> userForm = formFactory.form(WebUser.class).bindFromRequest();
			if(userForm.hasErrors()){
	            flash("danger", "Input validation failed.");
	            return badRequest(views.html.userCreate.render(userForm));
	        }
	        WebUser user = userForm.get();
			user.save();
			flash("success", "User Successfully Updated");
			return redirect(routes.HomeController.index());
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	public Result editUser (Long id) {
		try {
			WebUser user = WebUser.find.byId(id);
	        if(user == null){
	            return notFound(views.html._404.render());
	        }else {
				Form<WebUser> userForm = formFactory.form(WebUser.class).fill(user);
	        	return ok(views.html.userEdit.render(userForm));
	        }
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
    }
	
	public Result updateUser() {
		try{
			WebUser user = formFactory.form(WebUser.class).bindFromRequest().get();
			WebUser oldUser = WebUser.find.byId(user.id);
			
			if(oldUser == null) {
				return notFound(views.html._404.render());
			}else {
				oldUser.name = user.name;
				oldUser.surname = user.surname;
				oldUser.email = user.email;
				oldUser.password = user.password;
				oldUser.emailVerified = user.emailVerified;
				oldUser.update();
				return redirect(routes.HomeController.index());
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
    }
	
	public Result deleteUser(Long id) {
		try {
			WebUser user = WebUser.find.byId(id);
			if(user == null) {
				return notFound(views.html._404.render());
			}else {
				user.delete();
				return redirect(routes.UserController.userCardList());
			}
		}catch(Exception e) {
			return notFound(views.html._404.render());
		}
	}
	
	
	//API verify login, send entire user JSON
	public Result userVerifyLogin() {
		try {
			JsonNode jsonNode = request().body().asJson();
			List<WebUser> user = WebUser.find.query().where().conjunction().eq("email", jsonNode.findPath("email").asLong()).eq("password", jsonNode.findPath("password").asLong()).endJunction().findList();
			
			if(user != null) {
				return ok(Json.toJson(user));
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
		
			if(bidList != null) {
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
		
			if(wishlistItemList != null) {
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
		
			if(saleList != null) {
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
