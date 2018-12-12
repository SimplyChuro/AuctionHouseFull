package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

@Entity
public class Bids extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

    @Constraints.Required
	public Double amount;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
	public Date date;
    	
    @Constraints.Required
    public String status;
    
    //Foreign Keys
    @ManyToOne 
    public Users user;
    
    @ManyToOne @JsonIgnore
    public Products product;
    
	public static final Finder<Long, Bids> find = new Finder<>(Bids.class);

	
	//Methods
	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	public Boolean createBid(Users user, JsonNode objectNode) {
		product = Products.find.byId(objectNode.findPath("product_id").asLong());
		
		Double highestBid = 0.00;
		if(product.bids.size() != 0) {
			for(Bids bid : product.bids) {
				if(bid.amount > highestBid) {
					highestBid = bid.amount;
				}
			}
		} else {
			highestBid = product.startingPrice;
		}
		
		if(highestBid >= product.startingPrice) {
			if(highestBid >= amount) {
				return false;
			} else {
				this.user = user;
				date = new Date();
				status = "active";
				save();
				return true;
			}
		} else {
			return false;
		}
		
	}
	
	public Boolean updateBid(JsonNode objectNode) {
		product = Products.find.byId(objectNode.findPath("product_id").asLong());
		amount = objectNode.findPath("amount").asDouble();
		
		Double highestBid = 0.00;
		for(Bids bid : product.bids) {
			if(bid.amount > highestBid) {
				highestBid = bid.amount;
			}
		}
	
		if(highestBid >= amount) {
			return false;
		} else {
			date = new Date();
			update();
			return true;
		}
				
	}
	
}