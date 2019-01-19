package controllers;

import java.io.File;
import java.util.List;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import models.Bids;
import models.Category;
import models.Pictures;
import models.ProductCategory;
import models.Products;
import models.Reviews;
import models.Sales;
import models.Users;
import models.Wishlists;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class SaleController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private Users user;
	private Sales saleItem;
	
	@Inject
    public SaleController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get sale
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					saleItem = Sales.find.byId(id);
					return ok(Json.toJson(saleItem));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get sales
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> getAll() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					List<Sales> sales = user.sales;
					return ok(Json.toJson(sales));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Create sale
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					JsonNode objectNode = request().body().asJson().get("sale");	
					JsonNode productNode = objectNode.get("product");
					Long cat_id = productNode.get("category_id").asLong();
					
					saleItem = new Sales();
					saleItem.user = user;
					saleItem.saveSale(objectNode);
					
					Category childCategory = Category.find.byId(cat_id);
					Category parentCategory = Category.find.byId(childCategory.parent_id);
					
					ProductCategory categoryConnectionChild = new ProductCategory();
					categoryConnectionChild.product = saleItem.product;
					categoryConnectionChild.category = childCategory;
					categoryConnectionChild.save();
					
					ProductCategory categoryConnectionParent = new ProductCategory();
					categoryConnectionParent.product = saleItem.product;
					categoryConnectionParent.category = parentCategory;
					categoryConnectionParent.save();
		
					return ok(Json.toJson(saleItem));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update sale
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					JsonNode objectNode = request().body().asJson().get("sale");	
					JsonNode productNode = objectNode.get("product");
					
					Long cat_id = productNode.get("category_id").asLong();
					
					saleItem = Sales.find.byId(id);
					saleItem.updateSale(objectNode);
					
					Category childCategory = Category.find.byId(cat_id);
					Category parentCategory = Category.find.byId(childCategory.parent_id);
					
					for(ProductCategory category : saleItem.product.productcategory) {
						if(category.category.parent_id == null && category.category.id != childCategory.id) {
							category.category = childCategory;
							category.update();
						} else {
							if(category.category.id != parentCategory.id) {
								category.category = parentCategory;					
								category.update();
							}
						}
					}
					
					return ok(Json.toJson(saleItem));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete sale
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				user = LoginController.getUser();
				if(!user.admin && user.active) {
					saleItem = Sales.find.byId(id);
					Products product = saleItem.product;
					
					for(Pictures picture : product.pictures) {
						picture.delete();
					}
					
					for(Bids bid : product.bids) {
						bid.delete();
					}
					
					for(Wishlists wish : product.wishlists) {
						wish.delete();
					}
					
					for(Reviews review : product.reviews) {
						review.delete();
					}
					
					for(ProductCategory cat : product.productcategory) {
						cat.delete();
					}
					
					if(product.sale != null) {
						product.sale.delete();
					}
					
					product.delete();
					
					return ok(Json.toJson(""));
				} else {
					return badRequest();
				}
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
		
}