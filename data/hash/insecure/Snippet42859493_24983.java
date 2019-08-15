import java.security.*;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JavaApplication1 {

    public static String getMD5(byte[] plaintext) throws Exception{

        //init hash algorithm
        MessageDigest md = MessageDigest.getInstance("MD5");

        //compute hash
        byte[] hash = md.digest(plaintext);


        //display hash in hex
        System.out.println(tohex(hash));
        return hash;

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(getMD5(0111001101101000011001)); 
    }

    public static String tohex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
