package models;

import java.util.*;
import javax.persistence.*;

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
public class UserBid extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

    @Constraints.Required
	public Integer userBidAmount;
    
    @Constraints.Required
	public String userBidDate;
    
    @Constraints.Required
    public String status;
    
    //Foreign Keys
    @ManyToOne @JsonIgnore
    public WebUser webUserBidReference;
    
    @ManyToOne 
    public Product productBidReference;
    
	public static final Finder<Long, UserBid> find = new Finder<>(UserBid.class);
	
	public void setUserBidItemInfo(JsonNode jsonNode) {
		baseUserBidItemInfo(jsonNode);
	}
	
	public void setUserBidItemInfo(JsonNode jsonNode, WebUser user, Product product) {
		baseUserBidItemInfo(jsonNode);
		webUserBidReference = user;
		productBidReference = product;
	}
	
	public void baseUserBidItemInfo(JsonNode jsonNode) {
		userBidAmount = jsonNode.findPath("userBidAmount").asInt();
		userBidDate = jsonNode.findPath("userBidDate").asText();
		status = jsonNode.findPath("status").asText();
	}
	
	
	//Json builder for users with the biggest bids
	
	public ObjectNode toJsonProduct() {
		ObjectNode node = Json.newObject();
		ArrayNode user = Json.newArray();
		user.add(webUserBidReference.toJsonShort());
		
		node.put("id", id);
		node.put("userBidAmount", userBidAmount);
		node.put("userBidDate", userBidDate);
	    node.put("status", userBidAmount);
	    node.putArray("user").addAll(user);
	    
	    return node;
	}
	
	//Json Request Returns
	
//	public ObjectNode toJsonPersonal() {
//		ObjectNode node = Json.newObject();
//		ArrayNode product = Json.newArray();
//		product.add(productBidReference.toJsonFull());
//		
//		node.put("id", id);
//	    node.put("userBidAmount", userBidAmount);
//	    node.put("status", status);
//	    node.putArray("product").addAll(product);
//	    
//	    return node;
//	}
//	
//	
	
}
