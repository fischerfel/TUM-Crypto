import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Test {
    public static void main (String[] args) throws Exception {
        String secretAccessKey = "mykey";
        String data = "my data";
        byte[] secretKey = secretAccessKey.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] bytes = data.getBytes();
        byte[] rawHmac = mac.doFinal(bytes);
        System.out.println(Base64.encodeBytes(rawHmac));
    }
}
