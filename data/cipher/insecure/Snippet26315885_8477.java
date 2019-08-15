import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Crypt {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = 
    new byte[] { 'A', 'b', 'c', 'd', 'e', 'f', 'g',
    'h', 'i', 'j', 'k','l', 'm', 'n', 'o', 'p'};

public static String encrypt(String Data) throws Exception {
    Key key = generateKey();
    String keyForJS = Base64.encodeBase64String(keyValue);
    System.out.println("Key2 = " + keyForJS);
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    String encryptedValue = Base64.encodeBase64(encVal).toString();
    return encryptedValue;
}

private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGO);
    return key;
}

public static void main(String a[]) throws Exception
{
  System.out.println("Encryption = " + Crypt.encrypt("Test"));

}
}
