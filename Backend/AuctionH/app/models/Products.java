package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
	public Double mainBid;
	
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
	public Double startingPrice;
   
	
	//Foreign Keys
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")
    public List<Pictures> pictures;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public List<ProductCategory> category;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public List<Bids> bids;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public List<Wishlists> wishlists; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product")@JsonIgnore
    public List<Sales> sales;

	public static final Finder<Long, Products> find = new Finder<>(Products.class);
	
}
