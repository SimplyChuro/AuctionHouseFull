package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class Sales extends Model{

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
    public Users user;
    
    @OneToOne 
    public Products product;
	
	public static final Finder<Long, Sales> find = new Finder<>(Sales.class);
	
	//	Methods
	
	public void updateSale(JsonNode objectNode) {
		
		this.address = objectNode.findPath("address").asText();
		this.city = objectNode.findPath("city").asText();
		this.zipCode = objectNode.findPath("zipCode").asText();
		this.phone = objectNode.findPath("phone").asText();
		this.country = objectNode.findPath("country").asText();
		this.status = objectNode.findPath("status").asText();
		
		JsonNode productNode = objectNode.get("product");
		product.name = productNode.findPath("name").asText();
		product.description = productNode.findPath("description").asText();
		product.status = productNode.findPath("status").asText();
		product.color = productNode.findPath("color").asText();
		product.size = productNode.findPath("size").asText();
		product.startingPrice = productNode.findPath("startingPrice").asDouble();	
		product.featured = productNode.findPath("featured").asBoolean();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		try {
			TimeZone timeZone;
			timeZone = TimeZone.getTimeZone("GMT+0:00");
			TimeZone.setDefault(timeZone);
			product.publishDate = format.parse(productNode.findPath("publishDate").asText());
			product.expireDate = format.parse(productNode.findPath("expireDate").asText());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Pictures picture : product.pictures) {	
			Boolean checker = false;
			try {
				for(JsonNode pictureNode : productNode.get("pictures")) {

					if(picture.id == pictureNode.findPath("id").asLong()) {
						checker = true;
					}
				}
				
				if(!(checker == true)) {
					picture.delete();
				}
				
			} catch(Exception e) {
				
			}
		}
		
		for(JsonNode pictureNode : productNode.get("pictures")) {
			try {
				if(pictureNode.findPath("id").asLong() > 0) {
					
				} else {
					Pictures picture = new Pictures();
					picture.url = pictureNode.findPath("url").asText();
					picture.product = product;
					picture.save();
				}
			} catch(Exception e) {
				
			}
		}
		
		product.update();
		this.update();

	}
	
	
	public void saveSale(JsonNode objectNode) {
		
		this.address = objectNode.findPath("address").asText();
		this.city = objectNode.findPath("city").asText();
		this.zipCode = objectNode.findPath("zipCode").asText();
		this.phone = objectNode.findPath("phone").asText();
		this.country = objectNode.findPath("country").asText();
		this.status = objectNode.findPath("status").asText();
		
		JsonNode productNode = objectNode.get("product");
		product = new Products();
		
		product.name = productNode.findPath("name").asText();
		product.description = productNode.findPath("description").asText();
		product.status = productNode.findPath("status").asText();
		product.color = productNode.findPath("color").asText();
		product.size = productNode.findPath("size").asText();
		product.startingPrice = productNode.findPath("startingPrice").asDouble();	
		product.featured = productNode.findPath("featured").asBoolean();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		try {
			TimeZone timeZone;
			timeZone = TimeZone.getTimeZone("GMT+0:00");
			TimeZone.setDefault(timeZone);
			product.publishDate = format.parse(productNode.findPath("publishDate").asText());
			product.expireDate = format.parse(productNode.findPath("expireDate").asText());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		product.save();
		this.save();
		
		
		for(JsonNode pictureNode : productNode.get("pictures")) {
			Pictures picture = new Pictures();
			picture.url = pictureNode.findPath("url").asText();
			picture.product = product;
			picture.save();
			
		}

	}
	
}