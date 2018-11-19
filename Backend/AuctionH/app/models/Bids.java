package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @ManyToOne @JsonIgnore
    public Users user;
    
    @ManyToOne 
    public Products product;
    
	public static final Finder<Long, Bids> find = new Finder<>(Bids.class);

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
	public Boolean createBid(Users user, JsonNode objectNode) {
		product = Products.find.byId(objectNode.findPath("product_id").asLong());
		if(product.mainBid >= amount) {
			return false;
		} else {
			this.user = user;
			save();
			product.bidCount++;
			product.mainBid = amount;
			product.update();
			return true;
		}
	}
	
	public Boolean updateBid(JsonNode objectNode) {
		amount = objectNode.findPath("amount").asDouble();
		if(product.mainBid >= amount) {
			return false;
		} else {
			product.mainBid = amount;
			update();
			product.update();
			return true;
		}
	}
	
}