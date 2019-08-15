import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;


public class Encoder {


    public String encrypt(String message, String encryptionKey) throws Exception {  

        // handle the key 
        SecretKey secretKey = null;
        byte[] keyValueAsBytes  =  Arrays.copyOf(encryptionKey.getBytes("UTF-8"), 24);

        DESedeKeySpec keySpec = new DESedeKeySpec(keyValueAsBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede"); 
        secretKey = keyFactory.generateSecret(keySpec);

        // cipher 
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // encode
        byte[] plainText = message.getBytes("UTF-8");
        byte[] encryptedText = cipher.doFinal(plainText);       
        return Base64.encodeBase64String(encryptedText);
    }


    public static void main(String[] args) throws Exception{
        String secretKey = "bC5PEcLzvb+jY1FZWuP4pw50";
        String message = "subscriptionId=0214288302000000207";

        Encoder enc = new Encoder();
        System.out.println(enc.encrypt(message, secretKey));
        //returns:hw6JzwdvmjwORzmitXcQ6vsmskK6vtdIObu+KYiGW4D4DRwNGHEX2w==
    }
}
