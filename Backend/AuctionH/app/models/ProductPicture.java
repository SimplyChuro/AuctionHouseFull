package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class ProductPicture extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String pictureName;
    
    @Constraints.Required
  	public String pictureDirectory;
    
    //Foreign Keys
    @ManyToOne @JsonManagedReference
    public Product productPictureReference;
    
	public static final Finder<Long, ProductPicture> find = new Finder<>(ProductPicture.class);
	
}
