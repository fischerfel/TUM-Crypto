package decryption;

import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

    public class Decryption {
      public static void main(String args[]) throws Exception {


          File file = new File("ecryption.pdf");
          System.out.println(file.getAbsolutePath());
          System.out.println("user.dir is: " + System.getProperty("user.dir"));

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        FileInputStream fis   = new FileInputStream(new File("ecrypted.pdf"));
        long start = System.currentTimeMillis();
        System.out.print(start+"           ");
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        FileOutputStream fos  = new FileOutputStream(new File("decrypted.pdf"));
        long end = System.currentTimeMillis();
        System.out.print(end);


        byte[] b = new byte[8];
        int i;

        while ((i = cis.read(b)) != -1) {
          fos.write(b, 0, i);
        }
        fos.flush(); fos.close();
        cis.close(); fis.close();       
      }
}
