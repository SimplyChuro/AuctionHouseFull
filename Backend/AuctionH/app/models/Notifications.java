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
import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
public class Notifications extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

    @Constraints.Required
	public String message;
    
    @Constraints.Required
	public Date created;
    
    @Constraints.Required
	public Date expiresAt;
    
    @ManyToOne @JsonIgnore
    public Users user;
    
    @ManyToOne 
    public Products product;
    
	public static final Finder<Long, Bids> find = new Finder<>(Bids.class);

}