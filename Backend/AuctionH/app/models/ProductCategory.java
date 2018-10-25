package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

@Entity
public class ProductCategory extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String category;
    
    @Constraints.Required
	public String parentCategory;
	
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public Product productCategoryReference;
    
	public static final Finder<Long, ProductCategory> find = new Finder<>(ProductCategory.class);
	
	public ObjectNode toJson() {
	    ObjectNode node = Json.newObject();
	    node.put("id", id);
	    node.put("category", category);
	    node.put("parentCategory", parentCategory);
	    return node;
	}
}
