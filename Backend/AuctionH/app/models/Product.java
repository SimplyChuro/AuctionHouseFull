package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;


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
	
}
