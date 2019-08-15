import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64; 
import static com.google.common.io.BaseEncoding.base16; 

public class AwsSignatureGenerator {

    public static void main(String[] args) throws Exception {
        String signature = getSignature(getSignatureKey("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",
                                                        "20151229", 
                                                        "us-east-1", 
                                                        "s3"));
        System.out.println(signature);
    }

    public static String getStringToSign(){
        String s3Policy = "{ \"expiration\": \"2015-12-30T12:00:00.000Z\", \"conditions\": [ {\"bucket\": \"sigv4examplebucket\"}, [\"starts-with\", \"$key\", \"user/user1/\"], {\"acl\": \"public-read\"}, {\"success_action_redirect\": \"http://sigv4examplebucket.s3.amazonaws.com/successful_upload.html\"}, [\"starts-with\", \"$Content-Type\", \"image/\"], {\"x-amz-meta-uuid\": \"14365123651274\"}, {\"x-amz-server-side-encryption\": \"AES256\"}, [\"starts-with\", \"$x-amz-meta-tag\", \"\"], {\"x-amz-credential\": \"AKIAIOSFODNN7EXAMPLE/20151229/us-east-1/s3/aws4_request\"}, {\"x-amz-algorithm\": \"AWS4-HMAC-SHA256\"}, {\"x-amz-date\": \"20151229T000000Z\" } ] }";
        return new Base64().encodeAsString(s3Policy.getBytes());
    }

    public static byte[] HmacSHA256(String data, byte[] key) throws Exception  {
         String algorithm="HmacSHA256";
         Mac mac = Mac.getInstance(algorithm);
         mac.init(new SecretKeySpec(key, algorithm));
         return mac.doFinal(data.getBytes("UTF8"));
    }

    public static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception  {
         byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
         byte[] kDate    = HmacSHA256(dateStamp, kSecret);
         byte[] kRegion  = HmacSHA256(regionName, kDate);
         byte[] kService = HmacSHA256(serviceName, kRegion);
         byte[] kSigning = HmacSHA256("aws4_request", kService);
         return kSigning;
    }

    public static String getSignature(byte[] key) throws Exception{
        return base16().lowerCase().encode(HmacSHA256(getStringToSign(), key));
    }
}
