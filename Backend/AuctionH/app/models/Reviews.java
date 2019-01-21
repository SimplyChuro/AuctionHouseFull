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
public class Reviews extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

    @Constraints.Required
	public int rating;
    
    @Constraints.Required
	public String description;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
	public Date postDate;
    
	
    //Foreign Keys
    @ManyToOne 
    public Users user;
    
    @ManyToOne @JsonIgnore
    public Products product;
    
	public static final Finder<Long, Reviews> find = new Finder<>(Reviews.class);
	
	
	//Method	
	public void createReview(Users user, JsonNode objectNode) {
		product = Products.find.byId(objectNode.findPath("product_id").asLong());
		
		this.user = user;
		postDate = new Date();
		save();	
	}
	
}
