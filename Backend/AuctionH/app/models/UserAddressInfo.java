package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

@Entity
public class UserAddressInfo extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String street;
    
    @Constraints.Required
	public String city;
    
    @Constraints.Required
	public String zipCode;
    
    @Constraints.Required
	public String state;
    
    @Constraints.Required
	public String country;
    
    //Foreign Keys
    @OneToOne @JsonIgnore
    public WebUser webUserAddressReference;
	
	public static final Finder<Long, UserAddressInfo> find = new Finder<>(UserAddressInfo.class);
	
	public void setUserAddressInfo(JsonNode jsonNode) {
		street = jsonNode.findPath("street").textValue();
		city = jsonNode.findPath("city").textValue();
		zipCode = jsonNode.findPath("zipCode").textValue();
		state = jsonNode.findPath("state").textValue();
		country = jsonNode.findPath("country").textValue();
	}
	
	
//	public ObjectNode toJson() {
//	    ObjectNode node = Json.newObject();
//	    node.put("id", id);
//	    node.put("street", street);
//	    node.put("city", city);
//	    node.put("zipCode", zipCode);
//	    node.put("state", state);
//	    node.put("country", country);	    
//	    return node;
//	}
	
}
