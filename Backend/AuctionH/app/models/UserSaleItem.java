package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class UserSaleItem extends Model{

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
    
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public WebUser webUserSaleReference;
    
    @ManyToOne @JsonManagedReference
    public Product productSaleReference;
	
	public static final Finder<Long, UserSaleItem> find = new Finder<>(UserSaleItem.class);
}
