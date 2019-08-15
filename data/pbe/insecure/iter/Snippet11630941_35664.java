import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class PBKDF2 {

    public static void main(String[] args) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec("iamtwentycharacterss".toCharArray(),"50.eGIYr3ZpxpWw67utH17s/A==".getBytes(),50,64);
            SecretKey s = f.generateSecret(ks);
            Key k = new SecretKeySpec(s.getEncoded(),"HmacSHA1");
            System.out.println(new String(k.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }       
    }

}
