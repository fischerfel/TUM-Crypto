import java.security.InvalidAlgorithmParameterException;  
import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;

public class simple {

 public static void main(String[] args) {
   String Key = "1234567890";
   byte[] KeyData = Key.getBytes(); 
   String IV    = "\0\0\0\0\0\0\0\0";

   try { 
     SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
     // modo CBC
     Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
     try {
       try {    
         IvParameterSpec IVparam = new javax.crypto.spec.IvParameterSpec(IV.getBytes());                     
         cipher.init(Cipher.ENCRYPT_MODE, KS,IVparam);
       } catch (InvalidAlgorithmParameterException e) {System.out.println(e);};  
     } catch (InvalidKeyException e) {System.out.println(e);};  

    // get the text to encrypt
    String inputText = "HELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOUHELLOYOU";

    // encrypt message
    try {
      byte[] encrypted = cipher.doFinal(inputText.getBytes());
      Base64 b64 = new Base64();
      System.out.println("Java Ciphertext\n" + b64.encodeAsString(encrypted));
    } catch (IllegalBlockSizeException e) {System.out.println(e);}
    catch (BadPaddingException e) {System.out.println(e);}  
   }  
   catch (NoSuchPaddingException e) {}
   catch (NoSuchAlgorithmException e) {}
 }   

}
