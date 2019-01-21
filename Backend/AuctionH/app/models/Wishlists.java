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
public class Wishlists extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String status;
    
    //Foreign Keys
    @ManyToOne @JsonIgnore
    public Users user;
    
    @ManyToOne
    public Products product;
    
	public static final Finder<Long, Wishlists> find = new Finder<>(Wishlists.class);

	
	//Methods	
	public void createWishlist(Users user, JsonNode objectNode) {
		this.user = user;
		product = Products.find.byId(objectNode.findPath("product_id").asLong());
		save();
	}
	
}