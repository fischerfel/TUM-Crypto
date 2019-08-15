package aes;

import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;


public class AESencrpytion {

  //private static final byte[] keyValue = new byte[]{'S','e','c','r','e','t'};


  public static String encrypt(String data) throws Exception{
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    SecureRandom rand = new SecureRandom();
    keyGen.init(rand);
    Key key = keyGen.generateKey();
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encValue = cipher.doFinal(data.getBytes());
    String encryptedValue = new BASE64Encoder().encode(encValue);
    return encryptedValue;
  }

  public static String decrypt(String encData) throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    SecureRandom rand = new SecureRandom();
    keyGen.init(rand);
    Key key = keyGen.generateKey();
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decodedValue = new BASE64Decoder().decodeBuffer(encData);
    //ERROR HAPPENS HERE
    byte[] decValue = cipher.doFinal(decodedValue);
    String decryptedVal = new String(decValue);
    return decryptedVal;
  }
