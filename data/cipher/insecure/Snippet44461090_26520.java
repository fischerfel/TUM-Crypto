import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class encryptData {
  public static void main(String[] args) {

    String data="amount=10&expiryDate=20150101 151515&orderRefNum=11001&postBackURL=http://localhost:9081/local/status.php&storeId=28";
    String key="89OUITUPRL3I8H3G";

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    encryptedValue = new String(Base64.encodeBase64(cipher.doFinal(data.getBytes())));
  }
}
