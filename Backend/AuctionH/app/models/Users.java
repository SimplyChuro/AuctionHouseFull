package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.libs.Json;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Users extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String name;
    
    @Constraints.Required
	public String surname;
    
    @Column(unique = true)
    @Constraints.Required
	public String email;
    
    @Constraints.Required
	public String password;
    
    @Constraints.Required
	public Boolean emailVerified;
    
    @Constraints.Required
  	public String avatar;
      
    @Constraints.Required
  	public String gender;
  	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
  	public Date dateOfBirth;

  	@Constraints.Required
  	public String phoneNumber;
      
    @Constraints.Required
   	public Boolean phoneVerified;
    
	//Foreign Keys
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="user")
    public Address address; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") 
    public List<Bids> bids; 
	 
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") 
    public List<Wishlists> wishlists; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
    public List<Sales> sales; 
	
	public static final Finder<Long, Users> find = new Finder<>(Users.class);

}
