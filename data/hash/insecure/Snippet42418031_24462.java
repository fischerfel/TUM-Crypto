import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Md5Encryption 
{
    private static final String ALGORITHM = "md5";
    private static final String DIGEST_STRING = "HG58YZ3CR9";
    private static final String CHARSET_UTF_8 = "utf-8";
    private static final String SECRET_KEY_ALGORITHM = "DESede";
    private static final String TRANSFORMATION_PADDING = "DESede/CBC/PKCS5Padding";

/* Encryption Method */
public byte[] encrypt(String message) throws Exception 
{ 
    final MessageDigest md = MessageDigest.getInstance(ALGORITHM); 
    final byte[] digestOfPassword = md.digest(DIGEST_STRING.getBytes(CHARSET_UTF_8)); 
    final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24); 
    for (int j = 0, k = 16; j < 8;) { 
            keyBytes[k++] = keyBytes[j++]; 
    } 
    System.out.println(new String(keyBytes));
    final SecretKey key = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM); 
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]); 
    final Cipher cipher = Cipher.getInstance(TRANSFORMATION_PADDING); 
    cipher.init(Cipher.ENCRYPT_MODE, key, iv); 

    final byte[] plainTextBytes = message.getBytes(CHARSET_UTF_8);
    System.out.println(new String(plainTextBytes));
    final byte[] cipherText = cipher.doFinal(plainTextBytes); 

    //BASE64Encoder base64encoder = new BASE64Encoder();
    //return base64encoder.encode(cipherText);
    return cipherText; 
} 



/* Decryption Method */
    public String decrypt(byte[] message) throws Exception { 
        final MessageDigest md = MessageDigest.getInstance(ALGORITHM); 
        final byte[] digestOfPassword = md.digest(DIGEST_STRING.getBytes(CHARSET_UTF_8)); 
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24); 
        for (int j = 0, k = 16; j < 8;) { 
                keyBytes[k++] = keyBytes[j++]; 
        } 
        System.out.println(new String(keyBytes));
        final SecretKey key = new SecretKeySpec(keyBytes, SECRET_KEY_ALGORITHM); 
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]); 
        final Cipher decipher = Cipher.getInstance(TRANSFORMATION_PADDING); 
        decipher.init(Cipher.DECRYPT_MODE, key, iv); 

        final byte[] plainText = decipher.doFinal(message); 

        return new String(plainText); 
    }

    public static void main(String[] args) throws Exception {


        String text = "TEST STRING TO ENCRYPT";
        byte[] codedtext = new Md5Encryption().encrypt(text);
//        String codedtext = ".ªÉ…U$L§U`8Ë‰­?¦”›°„";
        String decodedtext = new Md5Encryption().decrypt(codedtext); 

        System.out.println(codedtext); // this is a byte array, you'll just see a reference to an array 
        System.out.println(decodedtext); // This correctly shows "TEST STRING TO ENCRYPT" 
    } 


}
