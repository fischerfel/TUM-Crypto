import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
public class TestJava {

   public static void main(String[] args) {
      try {
         String secret = "secret";
         String message = "Message";

         Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
         SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
         sha256_HMAC.init(secret_key);

         Base64.Encoder encoder = Base64.getEncoder();
         String hash = encoder.encodeToString(sha256_HMAC.doFinal(message.getBytes()));
         System.out.println(hash);
     } catch (Exception e){
       System.out.println("Error");
     }
  }
}
