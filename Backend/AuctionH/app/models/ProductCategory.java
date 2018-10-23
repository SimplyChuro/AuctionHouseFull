package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ProductCategory extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String category;
    
    @Constraints.Required
	public String parentCategory;
	
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public Product product;
    
	public static final Finder<Long, ProductCategory> find = new Finder<>(ProductCategory.class);
}
