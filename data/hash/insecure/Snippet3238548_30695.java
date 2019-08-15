import java.security.*;
import java.io.*;
public class MD5Hasher{
    public static void main(String[] args){
        String myString = "Hello, World!";
        byte[] myBA = myString.getBytes();
        MessageDigest myMD;
        try{
            myMD = MessageDigest.getInstance("MD5");
            myMD.update(myBA);
            byte[] newBA = myMD.digest();
            String output = newBA.toString();
            System.out.println("The Answer Is: " + output);
        } catch(NoSuchAlgorithmException nsae){
            // print error here
        }
    }
}
