import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class StringDecryptor {

    public static String encrypt(String text, String key) {
        Key aesKey = null;
        Cipher cipher = null;
        byte[] encrypted = null;
        try {
            aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(encrypted);
    }

    public static String decrypt(String text, String key) {
        Key aesKey = null;
        Cipher cipher;
        String decrypted = null;
        try {
            aesKey  = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            decrypted = new String(cipher.doFinal(text.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decrypted;
    }

    public static String generateKey() {
        SecretKey secretKey = null;
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        String keyString = bytesToString(secretKey.getEncoded());
        return keyString;
    }

    public static String bytesToString(byte[] b) {
        String decoded = null;
        try {
            decoded = new String(b, "UTF-8");            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return decoded;
    }

    public static void main(String args[]) {
        String key = generateKey();
        System.out.println("key: " + key);
        String str = "This is the original string...";
        String enc = encrypt(str, key);
        System.out.println("enc: " + enc);
        String dec = decrypt(enc, key);
        System.out.println("dec: " + dec);
    }
}
