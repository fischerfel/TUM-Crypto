import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESDemo {

    private static final String password = "test";
    private static String salt;
    private static int pswdIterations = 65536;
    private static int keySize = 256;
    //read from DB
    private byte[] ivBytes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    public String encrypt(String plainText) throws Exception {

        // get salt
        if (salt == null)
            salt = generateSalt();
        byte[] saltBytes = salt.getBytes("UTF-8");

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                pswdIterations, keySize);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // encrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(ivBytes));
        byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

        return new Base64().encodeAsString(encryptedTextBytes);
    }

    @SuppressWarnings("static-access")
    public String decrypt(String encryptedText) throws Exception {

        byte[] saltBytes = salt.getBytes("UTF-8");
        byte[] encryptedTextBytes = new Base64().decodeBase64(encryptedText);

        // Derive the key
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                pswdIterations, keySize);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // Decrypt the message
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret,
                new IvParameterSpec(ivBytes));

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
        //get the salt from DB
        return "I hate salt";
    }
}

public class stackoverflow_test {

    public static void main(String[] ag) throws Exception{
        AESDemo d = new AESDemo();

        System.out.println("Encrypted string:" + d.encrypt("brother crowd mean guy ancient demand society before perfection glare anger certain"));           
        String encryptedText = d.encrypt("brother crowd mean guy ancient demand society before perfection glare anger certain");
        String encryptedText2 = d.encrypt("Hello World");
        System.out.println("Decrypted string:" + d.decrypt(encryptedText2));        
        System.out.println("Decrypted string:" + d.decrypt(encryptedText));         

    }
}
