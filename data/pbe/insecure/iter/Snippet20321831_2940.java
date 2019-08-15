import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Helper {

   public static Cipher dcipher, ecipher;

   // Responsible for setting, initializing this object's encrypter and
   // decrypter Chipher instances
   Helper(String passPhrase) {

          // 8-bytes Salt
          byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                       (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

          // Iteration count
          int iterationCount = 19;

          try {
                 // Generate a temporary key. In practice, you would save this key
                 // Encrypting with DES Using a Pass Phrase
                 KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
                              iterationCount);
                 SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                              .generateSecret(keySpec);

                 ecipher = Cipher.getInstance(key.getAlgorithm());
                 dcipher = Cipher.getInstance(key.getAlgorithm());

                 // Prepare the parameters to the cipthers
                 AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                              iterationCount);

                 ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                 dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

          } catch (InvalidAlgorithmParameterException e) {
                 System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
          } catch (InvalidKeySpecException e) {
                 System.out.println("EXCEPTION: InvalidKeySpecException");
          } catch (NoSuchPaddingException e) {
                 System.out.println("EXCEPTION: NoSuchPaddingException");
          } catch (NoSuchAlgorithmException e) {
                 System.out.println("EXCEPTION: NoSuchAlgorithmException");
          } catch (InvalidKeyException e) {
                 System.out.println("EXCEPTION: InvalidKeyException");
          }
   }

   // Encrpt Password
   @SuppressWarnings("unused")
   protected String encrypt(String str) {
          try {
                 // Encode the string into bytes using utf-8
                 byte[] utf8 = str.getBytes("UTF8");
                 // Encrypt
                 byte[] enc = ecipher.doFinal(utf8);
                 // Encode bytes to base64 to get a string
                 return new sun.misc.BASE64Encoder().encode(enc);

          } catch (BadPaddingException e) {
          } catch (IllegalBlockSizeException e) {
          } catch (UnsupportedEncodingException e) {
          }
          return null;
   }

   // Decrpt password
   // To decrypt the encryted password
   protected String decrypt(String str) {
          Cipher dcipher = null;
          try {
                 byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                              (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };
                 int iterationCount = 19;
                 try {
                       String passPhrase = "";
                       KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(),
                                     salt, iterationCount);
                       SecretKey key = SecretKeyFactory
                                     .getInstance("PBEWithMD5AndDES")
                                     .generateSecret(keySpec);
                       dcipher = Cipher.getInstance(key.getAlgorithm());
                       // Prepare the parameters to the cipthers
                       AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                                     iterationCount);
                       dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
                 } catch (InvalidAlgorithmParameterException e) {
                       System.out
                                     .println("EXCEPTION: InvalidAlgorithmParameterException");
                 } catch (InvalidKeySpecException e) {
                       System.out.println("EXCEPTION: InvalidKeySpecException");
                 } catch (NoSuchPaddingException e) {
                       System.out.println("EXCEPTION: NoSuchPaddingException");
                 } catch (NoSuchAlgorithmException e) {
                       System.out.println("EXCEPTION: NoSuchAlgorithmException");
                 } catch (InvalidKeyException e) {
                       System.out.println("EXCEPTION: InvalidKeyException");
                 }
                 // Decode base64 to get bytes
                 byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
                 // Decrypt
                 byte[] utf8 = dcipher.doFinal(dec);
                 // Decode using utf-8
                 return new String(utf8, "UTF8");
          } catch (BadPaddingException e) {
          } catch (IllegalBlockSizeException e) {
          } catch (UnsupportedEncodingException e) {
          } catch (IOException e) {
          }
          return null;
   }

   /**
    * @param args
    */
   public static void main(String[] args) {
          try {

                 // Create encrypter/decrypter class
                 System.out.println("Inside Helper");
                 Helper encrypter = new Helper("");

                 // Pass the word to be Encrypted to Encrypt()
                 System.out.println("encrypt the String: SimplePassword");
                 String encrypted = encrypter.encrypt("SimplePassword");
                 System.out.println("encrypted String:" + encrypted);

                 // Pass the encrypted word to be Decrypted to Decrypt()
                 String decrypted = encrypter.decrypt(encrypted);
                 System.out.println("decrypted String:" + decrypted);
          } catch (Exception e) {
          }
   }
