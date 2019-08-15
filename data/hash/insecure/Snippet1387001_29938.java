import java.security.*;
import sun.misc.BASE64Encoder;
import java.io.*;
public class test {
  public static String encrypt(String password) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA");
      md.update(password.getBytes("UTF-8")); // step 3
      byte raw[] = md.digest(); // step 4
      String hash = (new BASE64Encoder()).encode(raw); // step 5
      return hash; // step 6
    } catch (NoSuchAlgorithmException e) {
    } catch (java.io.UnsupportedEncodingException e) {
    }
    return null;
  }

  public static void main(String[] args) {
    System.out.println(encrypt("my password"));
  }
}
