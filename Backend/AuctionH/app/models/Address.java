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
public class Address extends Model{
	
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
    public Users user;
	
	public static final Finder<Long, Address> find = new Finder<>(Address.class);

	
	//Constructor
	
	public Address() {
		
	}
	
	public Address(String street, String city, String zipCode, String state, String country) {
		this.street = street;
		this.city = city;
		this.zipCode = zipCode;
		this.state = state;
		this.country = country;
	}
		
	public void updateAddress(String street, String city, String zipCode, String state, String country) {
		
		if(street != null || !(street.equals("null"))) {
			if(!(street.trim().isEmpty())){
				this.street = street;
			}
		}
		
		if(city != null || !(city.equals("null"))) {
			if(!(city.trim().isEmpty())){
				this.city = city;
			}
		}
		
		if(zipCode != null || !(zipCode.equals("null"))) {
			if(!(zipCode.trim().isEmpty())){
				this.zipCode = zipCode;
			}
		}
		
		if(state != null || !(state.equals("null"))) {
			if(!(state.trim().isEmpty())){
				this.state = state;
			}
		}
		
		if(country != null || !(country.equals("null"))) {
			if(!(country.trim().isEmpty())){
				this.country = country;
			}
		}
		update();
	}
	
}