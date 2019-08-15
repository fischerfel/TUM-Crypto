import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class H {
    public static void main(String args[]){
        try{
        String strDefaultKey = "QabC-+50";
        Key key = new SecretKeySpec(strDefaultKey.getBytes("UTF-8"), "DES");
        Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        String seed = "2016-09-19 05:11";
        byte[] a = encryptCipher.doFinal(seed.getBytes());
        byte[] encodeUrl = Base64.getEncoder().encode(a);
//      byte[] encodeUrl = Base64.encodeBase64(sEncription.encrypt(m));
        String finalUrl = new String(encodeUrl);
        String finalResult = finalUrl.substring(2, 8) + finalUrl.substring(10, 13);
        System.out.println(finalUrl);
        System.out.println(finalResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
