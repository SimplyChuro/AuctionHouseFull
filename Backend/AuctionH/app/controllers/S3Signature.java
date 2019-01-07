package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.ec2.util.S3UploadPolicy;
import com.typesafe.config.Config;
import sun.misc.BASE64Encoder;

import org.jets3t.service.S3Service;
import org.jets3t.service.utils.ServiceUtils;
import org.jets3t.service.Constants;

import play.libs.Json;
import com.typesafe.config.ConfigException;
import javax.inject.Inject;
import com.typesafe.config.ConfigFactory;

public class S3Signature {
	
	public static final String AWS_S3_BUCKET = "aws.s3.bucket";
    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";

    public static AmazonS3 amazonS3;
	public String name;
	public String type;
	public Integer size;
	
	public S3Signature() {}

	public S3Signature(String name, String type, Integer size) {
		this.name = name;
		this.type = type;
		this.size = size;
	}
	

    public String getAmazonAccessKey() {
        if (ConfigFactory.load().hasPath(AWS_ACCESS_KEY)) {
            return ConfigFactory.load().getString(AWS_ACCESS_KEY);
        } else {
            throw new ConfigException.Missing(AWS_ACCESS_KEY);    
        }
    }
    
    public String getAmazonSecretKey() {
        if (ConfigFactory.load().hasPath(AWS_SECRET_KEY)) {
            return ConfigFactory.load().getString(AWS_SECRET_KEY);
        } else {
        	throw new ConfigException.Missing(AWS_SECRET_KEY);    
        }
    }

	public JsonNode getS3EmberNode() {
		String accessKey = getAmazonAccessKey();
        String secretKey = getAmazonSecretKey();
        String s3Bucket = "auction-house-storage";
        String service = "s3";
        String region = "eu-west-2";
        String requestType = "aws4_request";
             
        ObjectNode response = Json.newObject();
        
        try {
	        Date expiryTime = createExpireTime();
	
	        TimeZone tz = TimeZone.getTimeZone("UTC");
	        
	        DateFormat dateFormatISO = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
	        DateFormat dateFormatStamp = new SimpleDateFormat("yyyyMMdd");
	        dateFormatISO.setTimeZone(tz);
	        
	        String currentDateISO = dateFormatISO.format(new Date());
	        String dateStamp = dateFormatStamp.format(new Date());
	        String policy = createPolicy(s3Bucket, name, type, expiryTime);
	
	        response.put("acl", "public-read");
	        response.put("Content-Type", type);
	        response.put("Cache-Control", "max-age=630720000, public");
	        response.put("Content-Disposition", "attachment");
	        response.put("key", "images/users/"+name);
	        response.put("policy", policy);
	        response.put("expires", expiryTime.toString());
	        response.put("success_action_status", "201");
	        response.put("bucket", s3Bucket);
	        response.put("x-amz-credential", accessKey+"/"+dateStamp+"/"+region+"/"+service+"/"+requestType);
	        response.put("x-amz-algorithm", "AWS4-HMAC-SHA256");
	        response.put("x-amz-date", currentDateISO);
	        response.put("x-amz-signature", getSignatureKey(secretKey, dateStamp, region, service, policy));
	        
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	return response;
        }
	}
	
	
	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return mac.doFinal(data.getBytes("UTF8"));
	}

	static String getSignatureKey(String key, String dateStamp, String regionName, String serviceName, String policy) throws Exception {
	    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
	    byte[] kDate = HmacSHA256(dateStamp, kSecret);
	    byte[] kRegion = HmacSHA256(regionName, kDate);
	    byte[] kService = HmacSHA256(serviceName, kRegion);
	    byte[] kSigning = HmacSHA256("aws4_request", kService);
	    String signature = Hex.encodeHexString(HmacSHA256(policy, kSigning));
	    return signature;
	}
	
	private String createPolicy(String bucket, String name, String contentType, Date expiry) {
        try {
            String[] conditions = {
            	S3Service.generatePostPolicyCondition_Equality("bucket", bucket),
                S3Service.generatePostPolicyCondition_Equality("acl", "public-read"),
                S3Service.generatePostPolicyCondition_Equality("expires", expiry.toString()),
                S3Service.generatePostPolicyCondition_Equality("success_action_status", "201"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("key"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("Content-Type"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("Content-Disposition"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("x-amz-credential"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("x-amz-algorithm"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("x-amz-date"),
                S3Service.generatePostPolicyCondition_AllowAnyValue("Cache-Control"),
                S3Service.generatePostPolicyCondition_Range(0, 524288000)
            };
            
            String policyDocument = "{\"expiration\": \"" + ServiceUtils.formatIso8601Date(expiry) + "\", \"conditions\": [" + ServiceUtils.join(conditions, ",") + "]}";

            return ServiceUtils.toBase64(policyDocument.getBytes(Constants.DEFAULT_ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Date createExpireTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 24);

        return cal.getTime();
    }
    
    

}
