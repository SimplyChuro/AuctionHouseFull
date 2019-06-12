package actors;

import javax.inject.Named;
import javax.inject.Inject;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import java.util.List;
import java.util.Date;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import models.Products;
import models.Bids;
import models.Users;

import java.util.concurrent.TimeUnit;

public class SaleNotificationActor {

	private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject MailerClient mailerClient;
    @Inject
    public SaleNotificationActor(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
    	this.actorSystem.scheduler().schedule(
            Duration.create(1, TimeUnit.MINUTES),
            Duration.create(2, TimeUnit.HOURS),
            () -> sendNotification(),
            this.executionContext
        );
    }
    
    private void sendNotification() {
    	List<Products> products = Products.find.query().where()
				.conjunction()
				.le("expireDate", new Date())
				.not()
				.eq("status", "sold")
				.endJunction()
				.findList();
    	
    	for(Products product : products) {
    		Bids bidChecker = new Bids();
    		
    		Double highestBid = 0.00;
			if(product.bids.size() != 0) {
				for(Bids bid : product.bids) {
					if(bid.amount > highestBid) {
						highestBid = bid.amount;
						bidChecker = bid;
					}
				}	
				Email bidEmail = new Email()
				      .setSubject("You've won the bid for "+product.name+"!")
				      .setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
				      .addTo("Misster/Miss <"+bidChecker.user.getEmail()+">")
				      .setBodyText("A text message")
				      .setBodyHtml(
				    		  "<html>"
				      		+ "<body>"
				      		+ "<p>"
				      		+ "You have successfully won the bid for the given item! In order to obtain the item please login and click the link bellow!"
				      		+ "</p>"
				      		+ "<a href=https://auction-house-frontend.herokuapp.com/shop/product-payment/"+bidChecker.product.id+">Finish Payment</a>"
				      		+ "</body>"
				      		+ "</html>");
				
				if(product.sale != null) {
					Email sellerEmail = new Email()
						      .setSubject("The sale for "+product.name+" has ended!")
						      .setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
						      .addTo("Misster/Miss <"+product.sale.user.getEmail()+">")
						      .setBodyText("A text message")
						      .setBodyHtml(
						    		  "<html>"
						      		+ "<body>"
						      		+ "<p>"
						      		+ "Your item has been successfully sold! The given winner should pay in the next few days, and upon payment you shall be notified." 
						      		+ "</p>"
						      		+ "</body>"
						      		+ "</html>");
				    mailerClient.send(sellerEmail);
					
				    
					mailerClient.send(bidEmail);
				    product.status = "sold";
				    product.update();
				}
			} else {
				if(product.sale != null) {
					Email sellerEmail = new Email()
						      .setSubject("The sale for "+product.name+" has ended!")
						      .setFrom("Auction House <auctionhouse.atlantbh@gmail.com>")
						      .addTo("Misster/Miss <"+product.sale.user.getEmail()+">")
						      .setBodyText("A text message")
						      .setBodyHtml(
						    		  "<html>"
						      		+ "<body>"
						      		+ "<p>"
						      		+ "Sadly no one has bet on your item." 
						      		+ "</p>"
						      		+ "</body>"
						      		+ "</html>");
					
				    mailerClient.send(sellerEmail);
				}
			    product.status = "sold";
			    product.update();
				
			}
			
			
    	}
    	
    }

}