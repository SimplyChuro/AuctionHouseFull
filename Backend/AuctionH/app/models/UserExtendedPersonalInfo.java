package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class UserExtendedPersonalInfo extends Model{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @Constraints.Required
	public String avatarName;
    
    @Constraints.Required
	public String gender;
	
	@Constraints.Required
	public String dateOfBirth;

    @Constraints.Required
	public String phoneNumber;
    
    //Foreign Keys
    @OneToOne @JsonManagedReference
    public WebUser webUserExtInfoReference;
    
	public static final Finder<Long, UserExtendedPersonalInfo> find = new Finder<>(UserExtendedPersonalInfo.class);
}
