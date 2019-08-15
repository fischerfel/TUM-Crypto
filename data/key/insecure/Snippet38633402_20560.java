import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptDecryptAESAlgo {
    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[] { 'A', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p' };

    public String encrypt(String Data) throws Exception {
        String encryptedValue = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            encryptedValue = new BASE64Encoder().encode(encVal);
            return encryptedValue;
        } catch (Exception e) {
        }
        return encryptedValue;
    }

    public String decrypt(String encryptedData) throws Exception {
        String decryptedValue = "";
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception e) {
        }
        return decryptedValue;
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}
