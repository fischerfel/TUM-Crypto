import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    private static final String _algorithm = "AES";
    private static final String _password = "_pasword*";
    private static final String _salt = "_salt*";
    private static final String _keygen_spec = "PBKDF2WithHmacSHA1";
    private static final String _cipher_spec = "AES/CBC/PKCS5Padding";

    public static String encrypt(String data) throws Exception {
        Key key = getKey();
        System.out.println(key.toString());
        Cipher cipher = Cipher.getInstance(_cipher_spec);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal);
        System.out.println("Encrypted value of "+data+": "+encryptedValue);
        return encryptedValue;
    }

    public static void decrypt(String encryptedData) throws Exception {
        Key key = getKey();
        System.out.println(key.toString());
        Cipher cipher = Cipher.getInstance(_cipher_spec);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = cipher.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        System.out.println("Decrypted value of "+encryptedData+": "+decryptedValue);
    }

    private static Key getKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(_keygen_spec);
        KeySpec spec = new PBEKeySpec(_password.toCharArray(), _salt.getBytes(), 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), _algorithm);
        return secret;
    }

    public static void main(String []str) throws Exception {
        String value = encrypt("India@123");
        decrypt(value);
    }
}
