import java.security.AlgorithmParameters; 
import java.security.NoSuchAlgorithmException; 
import java.security.SecureRandom; 

import javax.crypto.BadPaddingException; 
import javax.crypto.Cipher; 
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.SecretKey; 
import javax.crypto.SecretKeyFactory; 
import javax.crypto.spec.IvParameterSpec; 
import javax.crypto.spec.PBEKeySpec; 
import javax.crypto.spec.SecretKeySpec; 
import javax.xml.bind.DatatypeConverter; 

public class EncryptionDecryption { 

    private static String salt; 
    private static int iterations = 65536  ; 
    private static int keySize = 256; 
    private static byte[] ivBytes; 

   // private static SecretKey secretKey; 
    private static final byte[] secretKey = "secret_key".getBytes(); 

    public static void main(String []args) throws Exception { 

        salt = getSalt(); 

        char[] message = "secret message".toCharArray(); 
        System.out.println("Message: " + String.valueOf(message)); 
        System.out.println("Encrypted: " + encrypt(message)); 
        System.out.println("Decrypted: " + decrypt(encrypt(message).toCharArray())); 
    } 

    public static String encrypt(char[] plaintext) throws Exception { 
        byte[] saltBytes = salt.getBytes(); 

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1"); 
        PBEKeySpec spec = new PBEKeySpec(plaintext, saltBytes, iterations, keySize); 
        //secretKey = skf.generateSecret(spec); 
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey, "AES"); 

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, secretSpec); 
        AlgorithmParameters params = cipher.getParameters(); 
        ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV(); 
        byte[] encryptedTextBytes = cipher.doFinal(String.valueOf(plaintext).getBytes("UTF-8")); 

        return DatatypeConverter.printBase64Binary(encryptedTextBytes); 
    } 

    public static String decrypt(char[] encryptedText) throws Exception { 

        System.out.println(encryptedText); 

        byte[] encryptedTextBytes = DatatypeConverter.parseBase64Binary(new String(encryptedText)); 
        SecretKeySpec secretSpec = new SecretKeySpec(secretKey, "AES"); 

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
        cipher.init(Cipher.DECRYPT_MODE, secretSpec, new IvParameterSpec(ivBytes)); 

        byte[] decryptedTextBytes = null; 

        try { 
            decryptedTextBytes = cipher.doFinal(encryptedTextBytes); 
        }   catch (IllegalBlockSizeException e) { 
            e.printStackTrace(); 
        }   catch (BadPaddingException e) { 
            e.printStackTrace(); 
        } 

        return new String(decryptedTextBytes); 

    } 

    public static String getSalt() throws Exception { 

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); 
        byte[] salt = new byte[20]; 
        sr.nextBytes(salt); 
        return new String(salt); 
    } 
}
