import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sun.security.provider.SecureRandom;

public class DerivationExample {

  private static SecretKey makeKeyFromUserInput(String userInput, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {

    SecretKeyFactory factory = SecretKeyFactory
        .getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(userInput.toCharArray(), salt, 1024, 256);
    byte[] derivedKey = factory.generateSecret(keySpec).getEncoded();
    return new SecretKeySpec(derivedKey, "AES");
  }

  public static void main(String[] args) throws Exception {
    String userInput = "foo";

    // PBKDF2 standard recommends at least 64-bit salt
    // Note: you want to randomly generate this elsewhere and keep it constant
    byte[] salt = new byte[8];
    new SecureRandom().engineNextBytes(salt);

    SecretKey derivedKey = makeKeyFromUserInput(userInput, salt);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, derivedKey, new IvParameterSpec(
        new byte[16]));

    String plaintext = "Hello, World!";
    byte[] cipherText = cipher.doFinal(plaintext.getBytes());

    // Derive key again to demonstrate it is the same
    SecretKey derivedKey2 = makeKeyFromUserInput(userInput, salt);
    cipher.init(Cipher.DECRYPT_MODE, derivedKey2, new IvParameterSpec(
        new byte[16]));

    byte[] plainText = cipher.doFinal(cipherText);
    // Prints "Hello, World!"
    System.out.println(new String(plainText));
  }
}
