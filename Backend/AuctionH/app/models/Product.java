package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;


@Entity
public class Product extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@Constraints.Required
	public String name;
	
	@Constraints.Required
	public String publishDate;
	
	@Constraints.Required
	public String expireDate;
	
	@Constraints.Required
	public Integer mainBid;
	
	@Constraints.Required
	public boolean active;
	
	@Column(columnDefinition = "varchar(1500)")
	@Constraints.Required
	public String mainDescription;
	
	@Column(columnDefinition = "varchar(1500)")
	@Constraints.Required
	public String additionalDescription;
	
	@Constraints.Required
	public Integer startingPrice;
   
	
	//Foreign Keys
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productCategoryReference") @JsonBackReference
    public List<ProductCategory> productCategory;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productPictureReference") @JsonBackReference
    public List<ProductPicture> productPictures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productBidReference") @JsonBackReference
    public List<UserBid> userBidsProducts;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productWishlistReference") @JsonBackReference
    public List<UserWishlistItem> userWishlistItemsProducts;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productSaleReference") @JsonBackReference
    public List<UserSaleItem> userSaleItemProducts;

	public static final Finder<Long, Product> find = new Finder<>(Product.class);
	
	public ObjectNode toJson() {
	    ObjectNode node = Json.newObject();
	    ObjectMapper mapper = new ObjectMapper();
	    ArrayNode categoryArray = Json.newArray();
	    for(ProductCategory category : productCategory) {
	    	categoryArray.add(category.toJson());
	    }
	    
	    ArrayNode pictureArray = Json.newArray();
	    for(ProductPicture picture : productPictures) {
	    	pictureArray.add(picture.toJson());
	    }
	    
	    node.put("id", id);
	    node.put("name", name);
	    node.put("publishDate", publishDate);
	    node.put("expireDate", expireDate);
	    node.put("mainBid", mainBid);
	    node.put("active", active);
	    node.put("mainDescription", mainDescription);
	    node.put("additionalDescription", additionalDescription);
	    node.put("startingPrice", startingPrice);
	    node.putArray("category").addAll(categoryArray);
	    node.putArray("pictures").addAll(pictureArray);
	    return node;
	}
	
}
