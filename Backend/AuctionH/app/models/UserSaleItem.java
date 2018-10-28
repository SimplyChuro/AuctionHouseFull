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
public class UserSaleItem extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String address;
    
    @Constraints.Required
	public String city;
    
    @Constraints.Required
	public String zipCode;
    
    @Constraints.Required
	public String phone;
    
    @Constraints.Required
	public String country;
    
    @Constraints.Required
	public String status;
    
    
    //Foreign Keys
    @ManyToOne  @JsonIgnore
    public WebUser webUserSaleReference;
    
    @ManyToOne 
    public Product productSaleReference;
	
	public static final Finder<Long, UserSaleItem> find = new Finder<>(UserSaleItem.class);
	
	public void setUserSaleItemInfo(JsonNode jsonNode) {
		baseUserSaleInfo(jsonNode);
	}
	
	public void setUserSaleItemInfo(JsonNode jsonNode, WebUser user, Product product) {
		baseUserSaleInfo(jsonNode);
		webUserSaleReference = user;
		productSaleReference = product;
	}
	
	public void baseUserSaleInfo(JsonNode jsonNode) {
		address = jsonNode.findPath("address").asText();
		city = jsonNode.findPath("city").asText();
		phone = jsonNode.findPath("phone").asText();
		country = jsonNode.findPath("country").asText();
		status = jsonNode.findPath("status").asText();
	}
	
	//Json Request Returns
	
//	public ObjectNode toJson() {
//		ObjectNode node = Json.newObject();
//		ArrayNode product = Json.newArray();
//		product.add(productSaleReference.toJsonFull());
//		
//		node.put("id", id);
//		node.put("address", address);
//		node.put("city", city);
//		node.put("zipCode", zipCode);
//		node.put("phone", phone);
//		node.put("country", country);
//	    node.put("status", status);
//	    node.putArray("product").addAll(product);
//	    
//	    return node;
//	}
}
