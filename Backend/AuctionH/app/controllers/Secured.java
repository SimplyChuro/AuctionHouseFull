package controllers;

import models.Users;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {
	
    @Override
    public String getUsername(Context ctx) {
        String authTokenHeaderValues = ctx.request().header(UserController.AUTH_TOKEN_HEADER).get();
        if ((authTokenHeaderValues != null)) {
            Users user = Users.findByAuthToken(authTokenHeaderValues);
            if (user != null) {
                ctx.args.put("user", user);
                return user.getEmail();
            }     
        }
       
        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized();
    }

}
