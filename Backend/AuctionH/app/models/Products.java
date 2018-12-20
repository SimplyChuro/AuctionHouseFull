package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.data.validation.Constraints.Required;
import play.libs.Json;


@Entity
public class Products extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@Constraints.Required
	public String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
	public Date publishDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
	public Date expireDate;
	
	@Constraints.Required
	public Double startingPrice;
	
	@Constraints.Required
	public String status;
	
	@Constraints.Required
	public String color;
	
	@Constraints.Required
	public String size;

	@Column(columnDefinition = "varchar(2048)")
	@Constraints.Required
	public String description;

	@Constraints.Required
	public Boolean featured;
	
	//Foreign Keys
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<Pictures> pictures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<ProductCategory> productcategory;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<Bids> bids;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public List<Wishlists> wishlists; 
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public Sales sale;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<Reviews> reviews;

	public static final Finder<Long, Products> find = new Finder<>(Products.class);

	
	//Constructor	
	public Products() {}
	
	public Products(String name, Date publishDate, Date expireDate,
			String status, String color, String size, String description, Double startingPrice, Boolean featured) {
		this.name = name;
		this.publishDate = publishDate;
		this.expireDate = expireDate;
		this.startingPrice = startingPrice;
		this.status = status;
		this.color = color;
		this.size = size;
		this.description = description;
		this.featured = featured;
	}
	
}