import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
  public static void main(String [] args) {
      try {
  String text = "test text 123";
  /*fixed here now it is 128 bits = 16 Bytes*/
  String encryptionKey = "E072EDF9534053A0";

  System.out.println("Before encryption: " + text);

  byte[] cipher = encrypt(text, encryptionKey);

  System.out.print("After encryption: ");
  for (int i=0; i<cipher.length; i++)
        System.out.print(new Integer(cipher[i])+" ");
  System.out.println("");

  String decrypted = decrypt(cipher, encryptionKey);

  System.out.println("After decryption: " + decrypted);

      } catch (Exception e) {
  e.printStackTrace();
      } 
  }

  public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(new byte[cipher.getBlockSize()]));
      return cipher.doFinal(plainText.getBytes("UTF-8"));
  }

  public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[cipher.getBlockSize()]));
      return new String(cipher.doFinal(cipherText),"UTF-8");
  }
  }
