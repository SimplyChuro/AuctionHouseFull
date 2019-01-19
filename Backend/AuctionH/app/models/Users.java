package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.libs.Json;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Users extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
    @JsonIgnore
	private String authToken;
	
	@Column(length = 256, nullable = false)
	@Constraints.Required
	@Constraints.MinLength(2)
	@Constraints.MaxLength(256)
	public String name;
    
    @Column(length = 256, nullable = false)
    @Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
	public String surname;
    
    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
	public String email;
    
    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
	private String password;
    
    @Constraints.Required
	public Boolean emailVerified;
    
    @Constraints.Required
  	public String avatar;
      
    @Constraints.Required
  	public String gender;
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	@Constraints.Required
  	public Date dateOfBirth;
	
	@Column(length = 256)
  	@Constraints.Required
  	public String phoneNumber;
      
    @Constraints.Required
   	public Boolean phoneVerified;
    
    @Constraints.Required
   	public Boolean admin;
    
    @Constraints.Required
   	public Boolean active;
    
	//Foreign Keys

	@OneToOne(fetch = FetchType.LAZY, mappedBy="user")
    public Address address; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") @JsonIgnore
    public List<Bids> bids; 
	 
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") @JsonIgnore
    public List<Wishlists> wishlists; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") @JsonIgnore
    public List<Sales> sales; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user") @JsonIgnore
    public List<Reviews> reviews; 
	
	public static final Finder<Long, Users> find = new Finder<>(Users.class);
    
	
	//	Getters Setters
	
	public void setBase() {
		emailVerified = false;
		admin = false;
		active = true;
		save();
		address = new Address("", "", "", "", "");
		address.user = this;
		address.save();
	}
	
	public void setBaseGoogle(JsonNode objectNode) {
		name = objectNode.findPath("given_name").asText();
		surname = objectNode.findPath("family_name").asText();
		email = objectNode.findPath("email").asText();
		avatar = objectNode.findPath("picture").asText();
		gender = objectNode.findPath("gender").asText();
		emailVerified = true;
		admin = false;
		active = true;
		save();
		address = new Address("", "", "", "", "");
		address.user = this;
		address.save();
	}
	
	public void setBaseFacebook(JsonNode objectNode) {
		name = objectNode.findPath("first_name").asText();
		surname = objectNode.findPath("last_name").asText();
		email = objectNode.findPath("email").asText();
		avatar = objectNode.get("picture").get("data").findPath("url").asText();
		emailVerified = true;
		admin = false;
		active = true;
		save();
		address = new Address("", "", "", "", "");
		address.user = this;
		address.save();
	}
	
	public void updateUser(JsonNode objectNode) {
		this.name = objectNode.findPath("name").asText();
		this.surname = objectNode.findPath("surname").asText();
		
		if(objectNode.findPath("gender").asText() != null || !(objectNode.findPath("gender").asText().equals("null"))) {
			this.gender = objectNode.findPath("gender").asText();	
		}
		
		if(objectNode.findPath("dateOfBirth").asText() != null || !(objectNode.findPath("dateOfBirth").asText().equals("null"))) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			
			try {
				TimeZone timeZone;
				timeZone = TimeZone.getTimeZone("GMT+0:00");
				TimeZone.setDefault(timeZone);
				this.dateOfBirth = format.parse(objectNode.findPath("dateOfBirth").asText());
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			this.dateOfBirth = null;
		}
		
		if(objectNode.findPath("phoneNumber").asText() != null && !(objectNode.findPath("phoneNumber").asText().equals("null"))) {	
			this.phoneNumber = objectNode.findPath("phoneNumber").asText();
		} else {
			this.phoneNumber = "";
		}
		
		if(objectNode.findPath("avatar").asText() != null && !(objectNode.findPath("avatar").asText().equals("null"))) {	
			this.avatar = objectNode.findPath("avatar").asText();
		} else {
			this.avatar = null;
		}
		
		this.update();
		this.address.updateAddress(
				objectNode.findPath("street").asText(),
				objectNode.findPath("city").asText(),
				objectNode.findPath("zipCode").asText(),
				objectNode.findPath("state").asText(),
				objectNode.findPath("country").asText()
			);
	}
	
	public void updatePassword(JsonNode objectNode) {
		this.setPassword(objectNode.findValue("password").asText());
		this.update();
	}
	
	
    //Token commands
    
    public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }
   
    public Boolean hasAuthToken() {
    	if(authToken != null) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    public String getAuthToken() {
    	return authToken;
    }
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
    }
    
	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
    

	//Token Based Getters	
	
    public static Users findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.query().where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

}