import java.math.*;
import java.security.*;
import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;

public class pwd {


public static void main(String[] args)throws NoSuchAlgorithmException, IOException,     InterruptedException {
    int count = 1;
    boolean run = true;
    while (run && count<4){

    System.out.println("Enter the password");
    Scanner kb = new Scanner(System.in);
    String pass = kb.nextLine();
    String pd = "ea0882721f7f44384ce772375696f9a6"; //Password is "csk" without quotes  geeks, this is it's MD5
    // so enter "csk" in the terminal
    // to run the program on execution
    String md5sum = md5(pass);

    String os = System.getProperty("os.name");
    boolean o = false;
    int win = os.indexOf("Windows");
    if (md5sum.equals(pd)){

        System.out.println("You've logged in successfully, get the Key now");
        String url = "file:///C:/Users/<username>/Desktop/key.html"; // example www
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        run = false;

    }
    else {

        System.out.println("You've entered the wrong password, try again.");
        System.out.println();
        run = true;

        if (count>=3) {
            System.out.println("You are banned from logging in, due to repeated  unsuccessful login attempts.");
        }
        ++count;


    }

    }

}




 public static String md5(String input)throws NoSuchAlgorithmException, IOException {

    String md5 = null;
    MessageDigest digest = MessageDigest.getInstance("MD5");
    digest.update(input.getBytes(), 0, input.length());
    md5 = new BigInteger(1, digest.digest()).toString(16);
   return md5;
 }



 }
