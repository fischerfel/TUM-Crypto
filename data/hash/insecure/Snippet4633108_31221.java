package ewa;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigInteger;
/**
 *
 * @author Lotus
 */
public class md5Hash {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String test = "abc";

        MessageDigest md = MessageDigest.getInstance("MD5");
        try {
            md.update(test.getBytes("UTF-8"));
            byte[] result = md.digest();
            BigInteger bi = new BigInteger(result);
            String hex = bi.toString(16);
            System.out.println("Pringting result");
            System.out.println(hex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(md5Hash.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
