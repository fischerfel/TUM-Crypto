create or replace and compile java source named testhmacSHA256 as
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
public class testhmacSHA256 {
  public static String encrypt(String secret, String message) { 
  try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
     SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
     sha256_HMAC.init(secret_key);

     String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
     return hash;
    }
    catch (Exception e){
     System.out.println("Error");
    }
  }
}
/
