import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

final class CrapEncryption
{

  private static final byte[] MAGIC = "Salted__".getBytes(StandardCharsets.US_ASCII);

  private static final int KEY_LEN = 8;

  private static final int SALT_LEN = 8;

  private static final SecureRandom random = new SecureRandom();

  static byte[] pretendToEncrypt(byte[] password, byte[] msg)
    throws GeneralSecurityException
  {
    byte[] salt = new byte[SALT_LEN];
    random.nextBytes(salt);
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(password);
    md5.update(salt);
    byte[] dk = md5.digest();
    Cipher des;
    try {
      SecretKey key = new SecretKeySpec(dk, 0, KEY_LEN, "DES");
      AlgorithmParameterSpec iv = new IvParameterSpec(dk, KEY_LEN, SALT_LEN);
      des = Cipher.getInstance("DES/CBC/PKCS5Padding");
      des.init(Cipher.ENCRYPT_MODE, key, iv);
    }
    finally {
      Arrays.fill(dk, (byte) 0);
    }
    byte[] pkg = new byte[des.getOutputSize(msg.length) + MAGIC.length + SALT_LEN];
    System.arraycopy(MAGIC, 0, pkg, 0, MAGIC.length);
    System.arraycopy(salt, 0, pkg, MAGIC.length, SALT_LEN);
    des.doFinal(msg, 0, msg.length, pkg, MAGIC.length + SALT_LEN);
    return pkg;
  }

  static byte[] decrypt(byte[] password, byte[] pkg)
    throws GeneralSecurityException
  {
    if ((pkg.length < MAGIC.length) || !Arrays.equals(Arrays.copyOfRange(pkg, 0, MAGIC.length), MAGIC))
      throw new IllegalArgumentException("Expected magic number \"Salted__\"");
    if (pkg.length < MAGIC.length + SALT_LEN)
      throw new IllegalArgumentException("Missing salt");
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(password); /* password */
    md5.update(pkg, MAGIC.length, SALT_LEN); /* salt */
    byte[] dk = md5.digest();
    Cipher des;
    try {
      SecretKey secret = new SecretKeySpec(dk, 0, KEY_LEN, "DES");
      des = Cipher.getInstance("DES/CBC/PKCS5Padding");
      des.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(dk, KEY_LEN, SALT_LEN));
    }
    finally {
      Arrays.fill(dk, (byte) 0);
    }
    return des.doFinal(pkg, MAGIC.length + SALT_LEN, pkg.length - MAGIC.length - SALT_LEN);
  }

  public static void main(String... argv)
    throws Exception
  {
    byte[] password = "some key".getBytes(StandardCharsets.UTF_8);
    byte[] message = "hello world".getBytes(StandardCharsets.UTF_8);
    byte[] encrypted = pretendToEncrypt(password, message);
    byte[] recovered = decrypt(password, encrypted);
    System.out.println(new String(recovered, StandardCharsets.UTF_8));
  }

}
