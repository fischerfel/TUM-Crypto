import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt
{

  public static void main(String... argv)
    throws Exception
  {
    byte[] password = "password".getBytes("UTF-8");
    byte[] ciphertext = { -68, -112,  66, 78,   85,   50, 22, -63, 
                           16,   24, -45,  4, -116,  -14, 88,  34, 
                          -85,  116, 105, 59,   45, -126 };
    byte[] plaintext = decrypt(password, ciphertext);
    System.out.println(new String(plaintext, "UTF-8"));
  }

  public static byte[] decrypt(byte[] password, byte[] ciphertext)
    throws GeneralSecurityException
  {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] hash = digest.digest(password);
    Cipher rc4 = Cipher.getInstance("RC4");
    rc4.init(Cipher.DECRYPT_MODE, new SecretKeySpec(hash, "RC4"));
    return rc4.doFinal(ciphertext);
  }

}
