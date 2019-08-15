import javax.crypto.*;    
import java.security.*;  
public class Java {

private static SecretKey key = null;         
   private static Cipher cipher = null; 

   public static void main(String[] args) throws Exception
   {

      Security.addProvider(new com.sun.crypto.provider.SunJCE());

      KeyGenerator keyGenerator =
         KeyGenerator.getInstance("DESede");
      keyGenerator.init(168);
      SecretKey secretKey = keyGenerator.generateKey();
      cipher = Cipher.getInstance("DESede");

      String clearText = "I am an Employee";
      byte[] clearTextBytes = clearText.getBytes("UTF8");

      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] cipherBytes = cipher.doFinal(clearTextBytes);
      String cipherText = new String(cipherBytes, "UTF8");

      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] decryptedBytes = cipher.doFinal(cipherBytes);
      String decryptedText = new String(decryptedBytes, "UTF8");

      System.out.println("Before encryption: " + clearText);
      System.out.println("After encryption: " + cipherText);
      System.out.println("After decryption: " + decryptedText);
   }
}


// Output

/*
Before encryption: I am an Employee  
After encryption: }?ｽ?ｽj6?ｽm?ｽZyc?ｽ?ｽ*?ｽ?ｽl#l?ｽdV  
After decryption: I am an Employee  
*/
