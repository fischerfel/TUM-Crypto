package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class Main  {


    private static final String ALGO ="AES";
    private static String string  = "TheBestSecretKey";
    private static  byte[] key ;
    private static SecretKeySpec secretKeySpec;



    public static String  aesEncrypt(String en) throws Exception {

        Key key = new SecretKeySpec(string.getBytes(),ALGO);
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encValue =c.doFinal(en.getBytes("UTF-8"));
        String encryptedValue= new BASE64Encoder().encode(encValue);
return encryptedValue;

    }

    public static String aesDecrypt(String De) throws Exception{

        Key key = new SecretKeySpec(string.getBytes(),"AES");

        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key);
       // return  new String(c.doFinal(Base64.getDecoder().decode(De)));

        byte[]decodedVlue=new BASE64Decoder().decodeBuffer(De);
        byte[] decValue = c.doFinal(decodedVlue);
        String deccryptedValue = new String(decValue);
        return deccryptedValue;

    }

    public static void main(String[] args) throws Exception {

        String password = "hello";
        String passEnc= Main.aesEncrypt(password);
        System.out.println(passEnc);
        String passDec = Main.aesDecrypt(passEnc);
        System.out.println(passDec);

    }
}
