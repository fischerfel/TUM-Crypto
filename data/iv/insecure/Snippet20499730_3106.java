package com.example.android.secure;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager {

 // we should get a password from the user
 String password = "...";
 String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
 // Important not to rely on default here !!!! use CBC instead of ECB
 String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
 int NUM_OF_ITERATIONS = 1000;
 int KEY_SIZE = 256;
 // generated on first run
 byte[] salt = "abababababababababa bab".getBytes();
 byte[] iv = "1234567890abcdef".getBytes();
 // This is the value to be encrypted.
 String clearText = "...";
 byte[] encryptedText;
 byte[] decryptedText;

 public void exampleCodeNoRealMethod() {
    try {
       PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, NUM_OF_ITERATIONS, KEY_SIZE);
       SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
       SecretKey tempKey = keyFactory.generateSecret(pbeKeySpec);
       SecretKey secretKey = new SecretKeySpec(tempKey.getEncoded(), "AES");
       IvParameterSpec ivSpec = new IvParameterSpec(iv);
       Cipher encCipher = Cipher.getInstance(CIPHER_ALGORITHM);
       encCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
       Cipher decCipher = Cipher.getInstance(CIPHER_ALGORITHM);
       decCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
       encryptedText = encCipher.doFinal(clearText.getBytes());
       decryptedText = decCipher.doFinal(encryptedText);
       String sameAsClearText = new String(decryptedText);
    } catch (Exception e) { 
       // TODO handle this exception
    }
 }

}
