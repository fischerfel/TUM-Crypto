import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
public class StrongAES 
{

    public static void main(String[] args) 
    {
         //listing all available cryptographic algorithms 

        /* for (Provider provider: Security.getProviders()) {
              System.out.println(provider.getName());
              for (String key: provider.stringPropertyNames())
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
            }*/
        StrongAES saes = new StrongAES();
        String encrypt = saes.encrypt(new String("Bar12346Bar12346"),new String("1234567812345678"));
        //String encrypt = saes.encrypt(new String("Bar12346Bar12346"),new String("Hello world"));
        System.out.println(encrypt);
        String decrypt = saes.decrypt(new String("Bar12346Bar12346"), new String(encrypt));
        System.out.println(decrypt);
    }


    String encrypt(String key, String text) 
    {
        String encryptedText="";
        try{
         // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");


        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        encryptedText = new String(encrypted);
       // System.out.println(encryptedText);
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
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        decryptedText = new String(cipher.doFinal(encryptedText.getBytes()));
       // System.out.println("Decrypted   "+decryptedText);

        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
        return decryptedText;

    }

}
