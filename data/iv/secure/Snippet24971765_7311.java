import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Aes { 
private static int pswdIterations = 65536;
private static int keySize =  256;

public static String encrypt(String plainText, String password, String salt, String initializationVector) throws 
    NoSuchAlgorithmException, 
    InvalidKeySpecException, 
    NoSuchPaddingException, 
    InvalidParameterSpecException, 
    IllegalBlockSizeException, 
    BadPaddingException, 
    UnsupportedEncodingException, 
    InvalidKeyException, 
    InvalidAlgorithmParameterException 
{   
    byte[] saltBytes = salt.getBytes("UTF-8");
    byte[] ivBytes = initializationVector.getBytes("UTF-8");

    // Derive the key, given password and salt.
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), 
            saltBytes, 
            pswdIterations, 
            keySize
    );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivBytes));

    byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
    return new Base64().encodeAsString(encryptedTextBytes);
}

    public static String decrypt(String encryptedText, String password, String salt, String initializationVector ) throws 
    NoSuchAlgorithmException, 
    InvalidKeySpecException, 
    NoSuchPaddingException, 
    InvalidKeyException, 
    InvalidAlgorithmParameterException,
    UnsupportedEncodingException
{
    byte[] saltBytes = salt.getBytes("UTF-8");
    byte[] ivBytes = initializationVector.getBytes("UTF-8");
    byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

    // Derive the key, given password and salt.
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), 
            saltBytes, 
            pswdIterations, 
            keySize
    );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    // Decrypt the message, given derived key and initialization vector.
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

    byte[] decryptedTextBytes = null;
    try {
        decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return new String(decryptedTextBytes);
   }  

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }

    public String generateIV(String chars, int length) {
    Random rand = new Random();
    StringBuilder buf = new StringBuilder();
    for (int i=0; i<length; i++) {
      buf.append(chars.charAt(rand.nextInt(chars.length())));
    }
    return buf.toString();
    }

    }
