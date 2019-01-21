package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
public class Pictures extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
		
    @Constraints.Required
	public String url;
    
    //Foreign Keys
    @ManyToOne @JsonIgnore
    public Products product;
    
	public static final Finder<Long, Pictures> find = new Finder<>(Pictures.class);

	
	//Constructors	
	public Pictures() {}
	
	public Pictures(String url, Products product) {
		this.url = url;
		this.product = product;
	}
	
}