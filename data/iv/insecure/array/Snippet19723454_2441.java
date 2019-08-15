// Java code - Cipher mode CBC version.
// CBC version need Initialization vector IV.
// Reference from https://stackoverflow.com/questions/6669181/why-does-my-aes-encryption-throws-an-invalidkeyexception/6669812#6669812

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class CryptoSecurity {

    public static String key = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    public static byte[] key_Array = Base64.decodeBase64(key);

    public static String encrypt(String strToEncrypt)
    {       
        try
        {   
            //Cipher _Cipher = Cipher.getInstance("AES");
            //Cipher _Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            Cipher _Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");        

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = { 1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key SecretKey = new SecretKeySpec(key_Array, "AES");    
            _Cipher.init(Cipher.ENCRYPT_MODE, SecretKey, ivspec);       

            return Base64.encodeBase64String(_Cipher.doFinal(strToEncrypt.getBytes()));     
        }
        catch (Exception e)
        {
            System.out.println("[Exception]:"+e.getMessage());
        }
        return null;
    }

    public static String decrypt(String EncryptedMessage)
    {
        try
        {
            //Cipher _Cipher = Cipher.getInstance("AES");
            //Cipher _Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            Cipher _Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");            

            // Initialization vector.   
            // It could be any value or generated using a random number generator.
            byte[] iv = { 1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 7, 7, 7, 7 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Key SecretKey = new SecretKeySpec(key_Array, "AES");
            _Cipher.init(Cipher.DECRYPT_MODE, SecretKey, ivspec);           

            byte DecodedMessage[] = Base64.decodeBase64(EncryptedMessage);
            return new String(_Cipher.doFinal(DecodedMessage));

        }
        catch (Exception e)
        {
            System.out.println("[Exception]:"+e.getMessage());          

        }
        return null;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();

        sb.append("xml file string ...");

        String outputOfEncrypt = encrypt(sb.toString());        
        System.out.println("[CryptoSecurity.outputOfEncrypt]:"+outputOfEncrypt);

        String outputOfDecrypt = decrypt(outputOfEncrypt);        
        //String outputOfDecrypt = decrypt(sb.toString());        
        System.out.println("[CryptoSecurity.outputOfDecrypt]:"+outputOfDecrypt);
    }

}
