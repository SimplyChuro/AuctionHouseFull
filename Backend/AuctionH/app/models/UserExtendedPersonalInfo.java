package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.libs.Json;

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
    
    @Constraints.Required
 	public Boolean phoneVerified;
    
    //Foreign Keys
    @OneToOne @JsonIgnore
    public WebUser webUserExtInfoReference;
    
	public static final Finder<Long, UserExtendedPersonalInfo> find = new Finder<>(UserExtendedPersonalInfo.class);

	public void setUserExtInfo(JsonNode jsonNode) {
		avatarName = jsonNode.findPath("avatarName").textValue();
		gender = jsonNode.findPath("gender").textValue();
		dateOfBirth = jsonNode.findPath("dateOfBirth").textValue();
		phoneNumber = jsonNode.findPath("phoneNumber").textValue();
		phoneVerified = jsonNode.findPath("phoneVerified").asBoolean();
	}
	
//	public ObjectNode toJson() {
//	    ObjectNode node = Json.newObject();
//	    node.put("id", id);
//	    node.put("avatarName", avatarName);
//	    node.put("gender", gender);
//	    node.put("dateOfBirth", dateOfBirth);
//	    node.put("phoneNumber", phoneNumber);
//	    return node;
//	}
}
