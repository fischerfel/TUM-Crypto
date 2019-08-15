import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;
import java.io.*;

class Encrypter {
public synchronized String encrypt(String plainText) throws Exception {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA");
    }catch(Exception exc) {
        throw new Exception(exc.getMessage());
     }

     try {
        md.update(plainText.getBytes("UTF-8"));
     }catch(Exception exc) {
        throw new Exception(exc.getMessage());
      }

      byte raw[] = md.digest();
      String hash = (new BASE64Encoder()).encode(raw);
      return hash;
}
public static void main(String args[]) {
    try {
        Encrypter encrypter = new Encrypter();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String userInput = br.readLine();
        String encryptedPassword = encrypter.encrypt(userInput);
        System.out.println(encryptedPassword);
    } catch(Exception exc) {
        System.out.println(exc);
      }
}
}
