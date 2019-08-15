import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
     public void run() {
         try {
             String text = "Hello World";
             String key = "1234567891234567";
             // Create key and cipher
             Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
             Cipher cipher = Cipher.getInstance("AES");

         // encrypt the text
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = cipher.doFinal(text.getBytes());
         System.out.println("Encrypted text: " + new String(encrypted));

         // decrypt the text
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted));
         System.out.println("Decrypted text: " + decrypted);
      }catch(Exception e) {
         e.printStackTrace();
      }
    }

    public static void main(String[] args) {
        AES app = new AES();
       app.run();
    }
}
