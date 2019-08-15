import java.io.*;
import java.security.*;

public class ReturnDigest extends Thread {

     private File input;
     private byte[] digest;

    public ReturnDigest(File input) {
     this.input = input;
    }

        public void run() {
          try {
        FileInputStream in = new FileInputStream(input);
        MessageDigest sha = MessageDigest.getInstance("SHA");
        DigestInputStream din = new DigestInputStream(in, sha);
        int b;
        while ((b = din.read()) != -1) ;
        din.close();
        digest = sha.digest();
      }
          catch (IOException ex) {
        System.err.println(ex);
      }
          catch (NoSuchAlgorithmException ex) {
        System.err.println(ex);
      }

    }

        public byte[] getDigest() {
      return digest;
    }

}
