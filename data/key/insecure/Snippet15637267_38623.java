import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESEncryptor {
    private static final String ALGO = "AES";
    private final static byte[] keyValue =new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

    public static String encrypt(String Data) throws Exception {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            byte[] encryptedValue = Base64.encodeBase64(encVal);
            String encryptedPass = new String (encryptedValue);
            return encryptedPass;
        }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        Base64.decodeBase64(encryptedData);
        byte[] decordedValue =  Base64.decodeBase64(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}


1st run :
argument passed to encrypt : somepassword|1364311519852
encrypted string : 5pQ1kIC+8d81AD7zbLOZA==(encrypted string)
decrypted string : somepassword|1364311519852

2nd run : 
argument passed to encrypt : somepassword|1364311695048
encrypted string : 5pQ1kIC+8d81AD7zbLOZA==(same encrypted string as before)
decrypted string : somepassword|1364311695048
