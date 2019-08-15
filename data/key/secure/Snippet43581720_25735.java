
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AES {


    public static SecretKeySpec secretKeySpec;
    private static byte[] key;


    public static String setKey(String myKey) throws Exception {


        MessageDigest sha=null;

        key =myKey.getBytes("UTF-8");
        sha=MessageDigest.getInstance("SHA-1");
        key=sha.digest(key);
        key= Arrays.copyOf(key,16);

        secretKeySpec=new SecretKeySpec(key,"AES");
        return myKey;
    }

    public static String encrypt(String strToencrypt, String secret) throws Exception {

        String s=  setKey(secret);
        System.out.println(s);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToencrypt.getBytes("UTF-8")));
    }


    public static  String decryt(String strToDec , String secret) throws Exception {
        setKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        return  new String(cipher.doFinal(Base64.getDecoder().decode(strToDec)));
    }

}
