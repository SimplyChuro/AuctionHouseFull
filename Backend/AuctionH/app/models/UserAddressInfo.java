package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class UserAddressInfo extends Model{
	
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
    @OneToOne @JsonManagedReference
    public WebUser webUser;
	
	public static final Finder<Long, UserAddressInfo> find = new Finder<>(UserAddressInfo.class);
}
