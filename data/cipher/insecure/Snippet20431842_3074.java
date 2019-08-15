import java.security.*;
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
public class Encrypt_Decrypt {
private static final String ALGORITHM = "AES";
private static final byte[] keyValue = 
    new byte[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
private static final String UNICODE_FORMAT  = "UTF8";

public static String encrypt(String valueToEnc) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = c.doFinal(valueToEnc.getBytes(UNICODE_FORMAT));
    String encryptedValue = new BASE64Encoder().encode(encValue);
    return encryptedValue;
}

private static Key generateKey() throws Exception {
    byte[] keyAsBytes;
    //keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    return key;
}

public static String decrypt(String encryptedValue) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue,"UTF8");
    return decryptedValue;
}
}
