import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class SO39257791
{

  private static final int KEY_LEN = 256 / 8, BLOCK_LEN = 16, ITERATIONS = 1000;

  public static void main(String... argv)
    throws Exception
  {
    String value = "YourId|YourFacId";
    String key = "6JxI1HOSg7KQj4fJ1Xb3L1T6AVdLZLBAPFSqOjh2UoA=";
    String salt = "FPSJxiSMpAavjKqyGvVe1A==";
    String good = "Y5w4A3pDZwTcq+FGyqUMO/mZSr6hSst8qiac9zDbfso9FQQbdTDsKnkKDT7SHl4y";

    String output = encrypt(value, key, salt);
    if (output.equals(good))
      System.out.println("strings are equal");
    else
      System.out.println("strings are NOT equal!");
  }

  static final String encrypt(String value, String key, String salt)
    throws GeneralSecurityException, UnsupportedEncodingException
  {
    /* Derive the key, given password and salt. */
    byte[] s = salt.getBytes(StandardCharsets.UTF_16LE);
    int dkLen = (KEY_LEN + BLOCK_LEN) * 8;
    KeySpec spec = new PBEKeySpec(key.toCharArray(), s, ITERATIONS, dkLen);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] dk = factory.generateSecret(spec).getEncoded();
    SecretKey secret = new SecretKeySpec(Arrays.copyOfRange(dk, 0, KEY_LEN), "AES");
    byte[] iv = Arrays.copyOfRange(dk, KEY_LEN, KEY_LEN + BLOCK_LEN);

    /* Encrypt the message. */
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
    byte[] plaintext = value.getBytes("UnicodeLittle"); /* Use Byte Order Mark */
    byte[] ciphertext = cipher.doFinal(plaintext);

    return Base64.getEncoder().encodeToString(ciphertext);
  }

}
