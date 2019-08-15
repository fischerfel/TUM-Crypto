package Test;
 import java.io.*;
 import java.security.*;
 import javax.crypto.*;
 import javax.crypto.spec.*;
 import java.util.*;

 public class FileDecryptor
 {
    private static String filename;
    private static String password;
    private static FileInputStream inFile;
    private static FileOutputStream outFile;

    public static void main(String[] args) throws Exception
    {

       // File to decrypt.

       filename = "Test.txt.des";

       String password = "super_secret_password";

       inFile = new FileInputStream(filename);
       outFile = new FileOutputStream(filename + ".dcr");

       PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
       SecretKeyFactory keyFactory =
           SecretKeyFactory.getInstance("PBEWithMD5AndDES");
       SecretKey passwordKey = keyFactory.generateSecret(keySpec);

       byte[] salt = new byte[8];
       inFile.read(salt);
       int iterations = 100;

       PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);


       Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
       cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);

       outFile.write(salt);


       byte[] input = new byte[64];
       int bytesRead;
       while ((bytesRead = inFile.read(input)) != -1)
       {
          byte[] output = cipher.update(input, 0, bytesRead);
          if (output != null)
             outFile.write(output);
       }
       byte[] output = cipher.doFinal();
       if (output != null)
          outFile.write(output);

       inFile.close();
       outFile.flush();
       outFile.close();
   }
}
