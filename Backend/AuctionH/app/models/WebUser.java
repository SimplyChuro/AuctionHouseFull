package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.libs.Json;
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
    
    @Constraints.Required
	public Boolean emailVerified;
    
    
	//Foreign Keys
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="webUserExtInfoReference")
    public UserExtendedPersonalInfo userExtendedPersonalInfo;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="webUserAddressReference")
    public UserAddressInfo userAddressInfo; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserBidReference") 
    public List<UserBid> userBids; 
	 
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserWishlistReference") 
    public List<UserWishlistItem> userWishlistItems; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="webUserSaleReference")
    public List<UserSaleItem> userSaleItems; 
	
	public static final Finder<Long, WebUser> find = new Finder<>(WebUser.class);
	

	
	
	
	
	//Json Request Returns
	
//	public ObjectNode toJsonFull() {
//	    ObjectNode node = Json.newObject();
//	    
//		ArrayNode extInfo = Json.newArray();
//		extInfo.add(userExtendedPersonalInfo.toJson());
//
//		ArrayNode addressInfo = Json.newArray();
//		addressInfo.add(userAddressInfo.toJson());
//	    
//	    node.put("id", id);
//	    node.put("name", name);
//	    node.put("surname", surname);
//	    node.put("email", email);
//	    node.put("password", password);
//	    node.putArray("extendedinfo").addAll(extInfo);
//	    node.putArray("addressinfo").addAll(addressInfo);
//	    return node;
//	}
//	
//	public ObjectNode toJson() {
//	    ObjectNode node = Json.newObject();
//	    node.put("id", id);
//	    node.put("name", name);
//	    node.put("surname", surname);
//	    node.put("email", email);
//	    node.put("password", password);
//	    return node;
//	}
//	
//	public ObjectNode toJsonShort() {
//	    ObjectNode node = Json.newObject();
//	    node.put("name", name);
//	    node.put("surname", surname);
//	    node.put("avatar", userExtendedPersonalInfo.avatarName);
//	    return node;
//	}
//	
//	
//	public ObjectNode toJsonUserBid() {
//	    ObjectNode node = Json.newObject();
//	    
//	    ArrayNode userBidArray = Json.newArray();
//	    for(UserBid bid : userBids) {
//	    	userBidArray.add(bid.toJsonPersonal());
//	    }
//	    
//	    node.putArray("userbids").addAll(userBidArray);
//	    return node;
//	}
//	
//	public ObjectNode toJsonUserWishlist() {
//	    ObjectNode node = Json.newObject();
//	    
//	    ArrayNode userWishlistArray = Json.newArray();
//	    for(UserWishlistItem item : userWishlistItems) {
//	    	userWishlistArray.add(item.toJson());
//	    }
//
//	    node.putArray("userwishlist").addAll(userWishlistArray);
//	    return node;
//	}
//	
//	public ObjectNode toJsonUserSale() {
//	    ObjectNode node = Json.newObject();
//	    
//	    ArrayNode userSaleArray = Json.newArray();
//	    for(UserSaleItem item : userSaleItems) {
//	    	userSaleArray.add(item.toJson());
//	    }
//
//	    node.putArray("usersales").addAll(userSaleArray);
//	    return node;
//	}
	
}
