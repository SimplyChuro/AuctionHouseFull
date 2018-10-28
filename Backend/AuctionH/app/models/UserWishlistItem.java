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
public class UserWishlistItem extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String status;
    
    //Foreign Keys
    @ManyToOne @JsonIgnore
    public WebUser webUserWishlistReference;
    
    @ManyToOne
    public Product productWishlistReference;
    
	public static final Finder<Long, UserWishlistItem> find = new Finder<>(UserWishlistItem.class);
	
	public void setUserWishlistItemInfo(JsonNode jsonNode) {
		baseUserWishlistItemInfo(jsonNode);
	}
	
	public void setUserWishlistItemInfo(JsonNode jsonNode, WebUser user, Product product) {
		baseUserWishlistItemInfo(jsonNode);
		webUserWishlistReference = user;
		productWishlistReference = product;
	}
	
	public void baseUserWishlistItemInfo(JsonNode jsonNode) {
		status = jsonNode.findPath("status").asText();
	}
	
	
	//Json Request Returns
//	
//	public ObjectNode toJson() {
//		ObjectNode node = Json.newObject();
//		ArrayNode product = Json.newArray();
//		product.add(productWishlistReference.toJsonFull());
//		
//		node.put("id", id);
//	    node.put("status", status);
//	    node.putArray("product").addAll(product);
//	    
//	    return node;
//	}
//	
}
