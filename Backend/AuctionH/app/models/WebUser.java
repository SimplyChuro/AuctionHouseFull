package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class WebUser extends Model{
	
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
    
    
	//Foreign Keys
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="webUserExtInfoReference") @JsonBackReference
    public List<UserExtendedPersonalInfo> userExtendedPersonalInfo;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="webUserAddressReference") @JsonBackReference
    public List<UserAddressInfo> userAddressInfo; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserBidReference") @JsonBackReference
    public List<UserBid> userBids; 
	 
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserWishlistReference") @JsonBackReference
    public List<UserWishlistItem> userWishlistItems; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserSaleReference") @JsonBackReference
    public List<UserSaleItem> userSaleItem; 
	
	public static final Finder<Long, WebUser> find = new Finder<>(WebUser.class);
	
}
