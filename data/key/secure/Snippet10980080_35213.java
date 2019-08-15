import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class JavaCrypt
{
    public static void main(String[] args) throws Exception {

           File f=new File("D:/a.txt");
           int ch;

               StringBuffer strContent = new StringBuffer("");
               FileInputStream fin = null;
               try {
               fin = new FileInputStream(f);
               while ((ch = fin.read()) != -1)
                   strContent.append((char) ch);
                   fin.close();
                   } 
               catch (Exception e) {
                   System.out.println(e);
                   }

               System.out.println("Original string: " +strContent.toString()+"\n");
               // Get the KeyGenerator

           KeyGenerator kgen = KeyGenerator.getInstance("AES");
           kgen.init(128); // 192 and 256 bits may not be available


           // Generate the secret key specs.
           SecretKey skey = kgen.generateKey();
           byte[] raw = skey.getEncoded();

           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");


           // Instantiate the cipher

           Cipher cipher = Cipher.getInstance("AES");

           cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

           byte[] encrypted = cipher.doFinal(strContent.toString().getBytes());

           System.out.println("encrypted string: " + encrypted.toString());

           cipher.init(Cipher.DECRYPT_MODE, skeySpec);
           byte[] original =cipher.doFinal(encrypted);

           String originalString = new String(original);
           System.out.println("Original string: " +originalString);
         }
}
