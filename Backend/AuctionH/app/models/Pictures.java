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
    
    @Constraints.Required
  	public Boolean main;
    
    //Foreign Keys
    @ManyToOne @JsonIgnore
    public Products product;
    
	public static final Finder<Long, Pictures> find = new Finder<>(Pictures.class);

	public Pictures() {}
	
	public Pictures(@Required String url, @Required Boolean main, Products product) {
		this.url = url;
		this.main = main;
		this.product = product;
	}
	
}