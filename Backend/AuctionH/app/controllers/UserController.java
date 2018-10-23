package controllers;

import java.util.List;

import javax.inject.Inject;

import models.WebUser;

import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;

public class UserController extends Controller {
	
	@Inject
	FormFactory formFactory;
	
	public Result userList() {
		List<WebUser> users = WebUser.find.all();
		return ok(Json.toJson(users));
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
		Form<WebUser> userForm = formFactory.form(WebUser.class);
		return ok(views.html.userCreate.render(userForm));
	}
	
	public Result save() {
		Form<WebUser> userForm = formFactory.form(WebUser.class).bindFromRequest();
		if(userForm.hasErrors()){
            flash("danger", "Input validation failed.");
            return badRequest(views.html.userCreate.render(userForm));
        }
        WebUser user = userForm.get();
		user.save();
		flash("success", "User Successfully Updated");
		return redirect(routes.HomeController.index());
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
		WebUser user = formFactory.form(WebUser.class).bindFromRequest().get();
		WebUser oldUser = WebUser.find.byId(user.id);
		
		if(oldUser == null) {
			return notFound(views.html._404.render());
		}else {
			oldUser.name = user.name;
			oldUser.surname = user.surname;
			oldUser.email = user.email;
			oldUser.password = user.password;
			oldUser.update();
		}
		return redirect(routes.HomeController.index());
    }
}
