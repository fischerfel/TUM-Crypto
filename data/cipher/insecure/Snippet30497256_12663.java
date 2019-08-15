import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class AES128Encryption {

    private static final String ALGO = "AES/ECB/PKCS5Padding";
    public static String decrypt(String encryptedData) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        byte[] raw = "************".getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        c.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
   }
