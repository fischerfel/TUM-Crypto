import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

class SecurityUtils {

  private static final byte[] salt = { (byte) 0xA4, (byte) 0x0B, (byte) 0xC8,
      (byte) 0x34, (byte) 0xD6, (byte) 0x95, (byte) 0xF3, (byte) 0x13 };

  private static int BLOCKS = 128;

  public static byte[] encryptAES(String seed, String cleartext)
      throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes("UTF8"));
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal(cleartext.getBytes("UTF8"));
  }

  public static byte[] decryptAES(String seed, byte[] data) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes("UTF8"));
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher.doFinal(data);
  }

  private static byte[] getRawKey(byte[] seed) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    sr.setSeed(seed);
    kgen.init(BLOCKS, sr); // 192 and 256 bits may not be available
    SecretKey skey = kgen.generateKey();
    byte[] raw = skey.getEncoded();
    return raw;
  }

  private static byte[] pad(byte[] seed) {
    byte[] nseed = new byte[BLOCKS / 8];
    for (int i = 0; i < BLOCKS / 8; i++)
      nseed[i] = 0;
    for (int i = 0; i < seed.length; i++)
      nseed[i] = seed[i];

    return nseed;
  }

  public static byte[] encryptPBE(String password, String cleartext)
      throws Exception {
    SecretKeyFactory factory = SecretKeyFactory
        .getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1024, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    return cipher.doFinal(cleartext.getBytes("UTF-8"));
  }

  public static String decryptPBE(SecretKey secret, String ciphertext,
      byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    return new String(cipher.doFinal(ciphertext.getBytes()), "UTF-8");
  }

}
