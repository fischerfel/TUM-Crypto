import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {

    public static byte[] encrypted(String t) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
        byte[] dataToSend = t.getBytes();
        byte[] key = new byte[16];
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        byte[] encryptedData = c.doFinal(dataToSend);
        return encryptedData; 
    }
    public static byte[] decrypted(byte[] kr) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
        byte[] key = new byte[16];
        SecretKeySpec k = new SecretKeySpec(key, "AES");
        byte[] encryptedData = kr;
        Cipher c2 = Cipher.getInstance("AES");
        c2.init(Cipher.DECRYPT_MODE, k);
        byte[] data = c2.doFinal(encryptedData);
        return data;
    }
    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

                //method1
                System.out.println(encrypted("adsda"));
                String f = new String (encrypted("adsda")); //working on console but not works when stores to cookies because of invalid characters
                System.out.println(f);
                System.out.println(new String(decrypted(f.getBytes())));// works when decrypting in console, not tried in cookies because not able encrypt


                //method2
                String x = encrypted("adsda").toString(); // works when stores in cookies works on console
                System.out.println(x);
                System.out.println(new String(decrypted(x.getBytes())));// decrypt not working both on console and cookies
                System.out.println(decrypted(x.getBytes()).toString()); // decrypt not working both on console and cookies


    }
}
