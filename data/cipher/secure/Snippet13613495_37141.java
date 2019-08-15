import java.security.*;
import java.math.BigInteger;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.crypto.Cipher;

public class testrsa
{
 public static void main(String [] args)
 {
   try
   {
     byte[] cipherData, plainData;
     KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
     kpg.initialize(4096);
     KeyPair kp = kpg.genKeyPair();
     Key publicKey = kp.getPublic();
     Key privateKey = kp.getPrivate();

      Cipher cipher1 = Cipher.getInstance("RSA");
      cipher1.init(Cipher.ENCRYPT_MODE, publicKey);
      cipherData = cipher1.doFinal("Hello, Word".getBytes());

      Cipher cipher2 = Cipher.getInstance("RSA");
      cipher2.init(Cipher.DECRYPT_MODE, privateKey);
      plainData = cipher2.doFinal(cipherData);

     System.out.print(plainData.toString());

     }
     catch(Exception ex)
     {
        System.out.print(ex.toString());
     }

 }
}
