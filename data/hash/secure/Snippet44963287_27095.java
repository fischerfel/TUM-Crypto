package generatehash;

import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

public class GenerateHash {

    public static void main(String[] args) 
    {
        String in = "abcdef12345";
        String salt = "test1";
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            byte[] digest = hash.digest((in + salt).getBytes());
            String out = new BASE64Encoder().encode(digest);
            System.out.println("Calculated: " + out);
        } catch(java.security.NoSuchAlgorithmException e) {
            System.err.println("SHA-256 is not a valid message digest algorithm. " + e.toString());
        }
    }
}
