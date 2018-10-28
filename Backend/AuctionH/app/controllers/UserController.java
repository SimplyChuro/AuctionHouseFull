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
	
}
