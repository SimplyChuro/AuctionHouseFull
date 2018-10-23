package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class UserBid extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

    @Constraints.Required
	public Integer userBidAmount;
    
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public WebUser webUser;
    
    @ManyToOne @JsonManagedReference
    public Product product;
    
	public static final Finder<Long, UserBid> find = new Finder<>(UserBid.class);
}
