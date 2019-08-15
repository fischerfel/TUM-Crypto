import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESEncryption {

    public static void main(String[] args) {
        try {
            String key = "AsKUMDPsr7dfuk6fxgpfRdwm6de+MArf4SdAds9aq";
            byte[] hashedKey = MessageDigest.getInstance("SHA1").digest(key.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(hashedKey, 2, 16, "AES"); // don't know how to port this to nodejs

            String plainText = "test123";
            Cipher cipher = Cipher.getInstance("AES");
            cipher1.init(1, keySpec);
            System.out.println(Base64.encodeBase64String(cipher.doFinal(plainText.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
