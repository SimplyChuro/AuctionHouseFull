package controllers;

import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import additions.Secured;
import models.Bids;
import models.Products;
import models.Users;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class BidController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private Users user;
	private Bids bidChecker;
	private List<Bids> bids;
	private JsonNode objectNode;
	
	@Inject MailerClient mailerClient;
	@Inject
    public BidController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get bid
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					Bids bid = Bids.find.byId(id);
					
					return ok(Json.toJson(bid));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get bids
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					bids = user.bids;
					return ok(Json.toJson(bids));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Create bid
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					objectNode = request().body().asJson().get("bid");
					bidChecker = Json.fromJson(objectNode, Bids.class);
					if(bidChecker.createBid(user, objectNode)) {
						bids = Bids.find.query().where()
								.conjunction()
								.eq("product_id", objectNode.findPath("product_id").asLong())
								.endJunction()
								.orderBy("amount desc")
								.findList();

						if(bids.size() >= 2) {
							Bids bidCheckerEmail = bids.get(1);
							Bids bidCheckerUser = bids.get(0);
							if(bidCheckerEmail.user.emailNotification != null) {
								if(bidCheckerEmail.user.emailNotification == true && (bidCheckerEmail.user != bidCheckerUser.user)) {
									Email email = new Email()
									      .setSubject("Someone just bid on "+bidCheckerEmail.product.name+" and overtook your bid!")
									      .setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
									      .addTo("Misster/Miss <"+bidCheckerEmail.user.getEmail()+">")
									      .setBodyText("A text message")
									      .setBodyHtml(
									    		  "<html>"
									      		+ "<body>"
									      		+ "<p>"
									      		+ "Reclaim your spot as the number one bidder!"
									      		+ "</p>"
									      		+ "<a href=https://auction-house-frontend.herokuapp.com/shop/single-product/"+bidCheckerEmail.product.id+">Bid on "+bidChecker.product.name+"</a>"
									      		+ "</body>"
									      		+ "</html>");
								    mailerClient.send(email);
								}
							}
						}
						
						return ok(Json.toJson(bidChecker));
					} else {
						return badRequest();
					}
				} else {
					return badRequest();	
				}
			} catch(Exception e) {
				e.printStackTrace();
				return badRequest();
			}
		}, httpExecutionContext.current());
	}	
	
	//Update bid	
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> update(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					objectNode = request().body().asJson().get("bid");
					bidChecker = Bids.find.byId(id);
					if(bidChecker != null) {
						bidChecker = Json.fromJson(objectNode, Bids.class);
						bidChecker.update();
						return ok(Json.toJson(bidChecker));
					} else {
						return badRequest();
					}		
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}	
	
	//Delete bid	
	@Security.Authenticated(Secured.class)	
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					bidChecker = Bids.find.byId(id);
					if(bidChecker != null) {
						bidChecker.delete();
						return ok(Json.toJson(""));
					} else {
						return badRequest();
					}		
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}	
}