import java.math.*;
import java.security.*;

public class testMain {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String stringThatNeedsToBeEncrpyted = "yourURL"; // Value to encrypt
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(stringThatNeedsToBeEncrpyted.getBytes(), 0, stringThatNeedsToBeEncrpyted.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16); //Make the Encrypted string
        System.out.println(md5); //print the string in the console

    }   
}
