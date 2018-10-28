package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
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
	public String status;
	
	@Constraints.Required
	public String color;
	
	@Constraints.Required
	public String size;


	@Column(columnDefinition = "varchar(1500)")
	@Constraints.Required
	public String mainDescription;
	
	@Column(columnDefinition = "varchar(1500)")
	@Constraints.Required
	public String additionalDescription;
	
	@Constraints.Required
	public Integer startingPrice;
   
	
	//Foreign Keys
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productCategoryReference") 
    public List<ProductCategory> productCategory;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productPictureReference")
    public List<ProductPicture> productPictures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productBidReference") @JsonIgnore
    public List<UserBid> userBidsProducts;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productWishlistReference")@JsonIgnore
    public List<UserWishlistItem> userWishlistItemsProducts; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="productSaleReference")@JsonIgnore
    public List<UserSaleItem> userSaleItemProducts;

	public static final Finder<Long, Product> find = new Finder<>(Product.class);
	
	
	public void setProductInfo(JsonNode jsonNode) {
		
		name = jsonNode.findPath("name").textValue();
		publishDate = jsonNode.findPath("publishDate").textValue();
		expireDate = jsonNode.findPath("expireDate").textValue();
		mainBid = jsonNode.findPath("mainBid").asInt();
		status = "active";
		color = jsonNode.findPath("color").textValue();
		size = jsonNode.findPath("size").textValue();
		mainDescription = jsonNode.findPath("mainDescription").textValue();
		additionalDescription = jsonNode.findPath("additionalDescription").textValue();
		startingPrice = jsonNode.findPath("startingPrice").asInt();
		
		this.save();
		
		for (JsonNode category : jsonNode.withArray("category")) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.category =  category.get("category").asText();
			productCategory.parentCategory = category.get("parentcategory").asText();
			productCategory.productCategoryReference = this;
			productCategory.save();
		}
		
		for (JsonNode picture : jsonNode.withArray("pictures")) {
			ProductPicture productPicture = new ProductPicture();
			productPicture.pictureName =  picture.get("pictureName").asText();
			productPicture.pictureDirectory = picture.get("pictureDirectory").asText();
			productPicture.pictureDirectory = picture.get("type").asText();
			productPicture.productPictureReference = this;
			productPicture.save();
		}
		
	}
	
	
//	public ObjectNode toJsonFull() {
//	    ObjectNode node = Json.newObject();
//	    ArrayNode categoryArray = Json.newArray();
//	    for(ProductCategory category : productCategory) {
//	    	categoryArray.add(category.toJson());
//	    }
//	    
//	    ArrayNode pictureArray = Json.newArray();
//	    for(ProductPicture picture : productPictures) {
//	    	pictureArray.add(picture.toJson());
//	    }
//	    
//	    node.put("id", id);
//	    node.put("name", name);
//	    node.put("publishDate", publishDate);
//	    node.put("expireDate", expireDate);
//	    node.put("mainBid", mainBid);
//	    node.put("active", active);
//	    node.put("mainDescription", mainDescription);
//	    node.put("additionalDescription", additionalDescription);
//	    node.put("startingPrice", startingPrice);
//	    node.putArray("category").addAll(categoryArray);
//	    node.putArray("pictures").addAll(pictureArray);
//	    return node;
//	}
	
//	public ObjectNode toJson() {
//		ObjectNode node = Json.newObject();
//		node.put("id", id);
//	    node.put("name", name);
//	    node.put("publishDate", publishDate);
//	    node.put("expireDate", expireDate);
//	    node.put("mainBid", mainBid);
//	    node.put("active", active);
//	    node.put("mainDescription", mainDescription);
//	    node.put("additionalDescription", additionalDescription);
//	    node.put("startingPrice", startingPrice);
//	    return node;
//	}
//	
//	public ObjectNode toJsonProductBidders() {
//		ObjectNode node = Json.newObject();
//	    ArrayNode userBidArray = Json.newArray();
//	    for(UserBid bid : userBidsProducts) {
//	    	userBidArray.add(bid.toJsonProduct());
//	    }
//		
//		node.put("id", id);
//	    node.put("name", name);
//	    node.put("publishDate", publishDate);
//	    node.put("expireDate", expireDate);
//	    node.put("mainBid", mainBid);
//	    node.put("active", active);
//	    node.put("mainDescription", mainDescription);
//	    node.put("additionalDescription", additionalDescription);
//	    node.put("startingPrice", startingPrice);
//	    node.putArray("bids").addAll(userBidArray);
//	    return node;
//	}
	
}
