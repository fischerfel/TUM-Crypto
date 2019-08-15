import javax.crypto.Cipher;         
import javax.crypto.spec.SecretKeySpec;         

import org.apache.commons.codec.DecoderException;           
import org.apache.commons.codec.binary.Hex;         

import sun.misc.BASE64Decoder;          
import sun.misc.BASE64Encoder;          


public class AESTest {          
    private static String sKeyString = "29c4e20e74dce74f44464e814529203a";      
    private static SecretKeySpec skeySpec = null;       

    static {        
        try {   
            skeySpec = new SecretKeySpec(Hex.decodeHex(sKeyString.toCharArray()), "AES");
        } catch (DecoderException e) {  
            e.printStackTrace();
        }   
    }       

    public static String encode(String message) {       
        String result = ""; 

        try {   
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
            result =  (new BASE64Encoder()).encode(encrypted);
        } catch (Exception e) { 
            e.printStackTrace();
        }   

        return result;  
    }       

    public static String decode(String message){        
        String result = ""; 
        try {   
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = (new BASE64Decoder()).decodeBuffer(message);
            byte[] original = cipher.doFinal(encrypted);
            result = new String(original,"UTF-8");
        } catch (Exception e) { 
            e.printStackTrace();
        }   

        return result;  
    }       

    public static void main(String[] args) {        
        String message = "SOME TEST";   
        System.out.println("message : "+message);   
        String encodeString = encode(message);  
        System.out.println("encrypted string: " + encodeString);    
        String original = decode(encodeString); 
        System.out.println("Original string: " + original); 
    }       
}           
