package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

@Entity
public class ProductPicture extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String pictureName;
    
    @Constraints.Required
  	public String pictureDirectory;
    
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public Product productPictureReference;
    
	public static final Finder<Long, ProductPicture> find = new Finder<>(ProductPicture.class);
	
	public ObjectNode toJson() {
	    ObjectNode node = Json.newObject();
	    node.put("id", id);
	    node.put("pictureName", pictureName);
	    node.put("pictureDirectory", pictureDirectory);
	    return node;
	}
	
}
