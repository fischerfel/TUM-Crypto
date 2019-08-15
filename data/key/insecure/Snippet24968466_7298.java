import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Test {

    private byte[] encrypted;

    private String encryptedtext;
    private String decrypted;



    public String Encrypt (String pInput) {


      try {

         String Input = pInput;
         String key = "Bar12345Bar12345Bar12345Bar12345"; 

         // Erstelle key and cipher
         SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");

         // Verschlüsselung
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = cipher.doFinal(Input.getBytes());
         encryptedtext = new String(encrypted);
         System.err.println("encrypted:" + encryptedtext);


      }catch(Exception e) {
         e.printStackTrace();
      }

        return encrypted;
    }



    public String Decrypt (String pInput) {


       try {

           String Input = pInput; 

           String key = "Bar12345Bar12345Bar12345Bar12345"; 

           // Erstelle key and cipher
           SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
           Cipher cipher = Cipher.getInstance("AES");

           // Entschlüsselung
           cipher.init(Cipher.DECRYPT_MODE, aesKey);
           decrypted = new String(cipher.doFinal(encryptedtext)); // HERE IS THE PROBLEM IT WANT BYTE BUT I WANT TO ENCRYPT FROM A STRING
           System.err.println("decrypted: " + decrypted);

        }catch(Exception e) {
           e.printStackTrace();
        }
        return pInput;
      }

}
