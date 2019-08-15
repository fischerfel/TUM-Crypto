import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class StrongAES 
{

    public static void main(String[] args) 
    {
        StrongAES saes = new StrongAES();
        String encrypt = saes.encrypt(new String("Bar12346Bar12346"),new String("1234567812345678"));
        System.out.println(encrypt);
        String decrypt = saes.decrypt(new String("Bar12346Bar12346"), new String(encrypt));
        System.out.println(decrypt);
    }


    String encrypt(String key, String text) 
    {
        String encryptedText="";
        try{
         // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes("utf-8"));
        BASE64Encoder encoder = new BASE64Encoder();
        encryptedText = encoder.encodeBuffer(encrypted);
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        return encryptedText;

    }

    String decrypt(String key, String encryptedText)
    {
        String decryptedText="";
        try{
         // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        BASE64Decoder decoder = new BASE64Decoder();
        decryptedText = new String(cipher.doFinal(decoder.decodeBuffer(encryptedText)));
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        return decryptedText;

    }

}
