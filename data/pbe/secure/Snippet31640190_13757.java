import java.net.*;
import java.io.*;
import javax.swing.*;
import java.util.regex.*;
import java.util.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class main {
  public static void main(String[] args) throws Exception {
    byte[] data = "hello".getBytes("UTF-8");
    printHex(data);

    Random ranGen = new SecureRandom();
    byte[] salt = new byte[8]; // 8 grains of salt
    ranGen.nextBytes(salt);

    String pw = "pw";
    byte[] enc = encrypt(data, pw.toCharArray(), salt);
    printHex(enc);
    System.out.println("enc length: " + enc.length);

    byte[] dec = decrypt(enc, pw.toCharArray(), salt);
    System.out.println("decrypted: " + new String(dec, "UTF-8"));
  }

  static void printHex(byte[] data) {
    System.out.println(bytesToHex(data));
  }

  static String bytesToHex(byte[] bytes) {
    return bytesToHex(bytes, 0, bytes.length);
  }

  static String bytesToHex(byte[] bytes, int ofs, int len) {
    StringBuilder stringBuilder = new StringBuilder(len*2);
    for (int i = 0; i < len; i++) {
      String s = "0" + Integer.toHexString(bytes[ofs+i]);
      stringBuilder.append(s.substring(s.length()-2, s.length()));
    }
    return stringBuilder.toString();
  }

  static SecretKey makeKey(char[] password, byte[] salt) throws Exception {
    /* Derive the key, given password and salt. */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    // only with unlimited strength:
    //KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);

    // Let's try this:
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);

    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    return secret;
  }

  public static byte[] encrypt(byte[] data, char[] password, byte[] salt) {
    try {
      SecretKey secret = makeKey(password, salt);

      /* Encrypt the message. */
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secret);
      AlgorithmParameters params = cipher.getParameters();
      byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(cipher.update(data));
      baos.write(cipher.doFinal());
      byte[] ciphertext = baos.toByteArray();
      return ciphertext;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static byte[] decrypt(byte[] ciphertext, char[] password, byte[] salt) {
    try {
      SecretKey secret = makeKey(password, salt);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secret);
      AlgorithmParameters params = cipher.getParameters();
      byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

      /* Decrypt the message, given derived key and initialization vector. */
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
      baos.write(cipher.update(ciphertext));
      baos.write(cipher.doFinal());
      return baos.toByteArray(); 
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
