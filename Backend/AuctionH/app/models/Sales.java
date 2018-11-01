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
    
    @ManyToOne 
    public Products product;
	
	public static final Finder<Long, Sales> find = new Finder<>(Sales.class);
	
}
