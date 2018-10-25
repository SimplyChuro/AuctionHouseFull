package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class UserWishlistItem extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String status;
    
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public WebUser webUserWishlistReference;
    
    @ManyToOne @JsonManagedReference
    public Product productWishlistReference;
    
	public static final Finder<Long, UserWishlistItem> find = new Finder<>(UserWishlistItem.class);
}
