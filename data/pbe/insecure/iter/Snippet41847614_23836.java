import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.instana.backend.common.exception.InstanaException;

public class AESTest {

  private static String pwd = "etjrgp9user9fu3984h1&(/&%$§";

  public static void main(String[] args) throws Exception {
    System.out.println("UNSECURE WITH ECB:");
    String ecbEncrypt = encrypt("YOLO", cypher(Cipher.ENCRYPT_MODE, "AES"));
    System.out.println("Encrypted: " + ecbEncrypt);

    String ebcDecrypt = decrypt(ecbEncrypt, cypher(Cipher.DECRYPT_MODE, "AES"));
    System.out.println("Decrypted: " + ebcDecrypt);

    System.out.println("=====================================");
    System.out.println("SECURE WITH CBC");

    String cbcEncrypt = encrypt("YOLO", cypher(Cipher.ENCRYPT_MODE, "AES/CBC/PKCS5Padding"));
    System.out.println("Encrypted: " + cbcEncrypt);

    String cbcDecrypt = decrypt(cbcEncrypt, cypher(Cipher.DECRYPT_MODE, "AES/CBC/PKCS5Padding"));
    System.out.println("Decrypted: " + cbcDecrypt);

    System.out.println("=====================================");
    System.out.println("Decrypting CBC with ECB");


  }

  public static String encrypt(String superDuperSecret, Cipher cipher) throws IOException {
    try {
      byte[] encrypted = cipher.doFinal(superDuperSecret.getBytes("UTF-8"));

      return new String(new Hex().encode(encrypted));

    } catch (Exception e) {
      throw new InstanaException("Encryption of token failed.", e);
    }
  }

  public static String decrypt(String superDuperSecret, Cipher cipher) {
    try {
      byte[] encrypted1 = new Hex().decode(superDuperSecret.getBytes("UTF-8"));

      return new String(cipher.doFinal(encrypted1));

    } catch (Exception e) {
      throw new InstanaException("Encrypted text could not be decrypted.", e);
    }
  }

  private static Cipher cypher(int mode, String method)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException,
      InvalidAlgorithmParameterException {
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(pwd.toCharArray(), pwd.getBytes(), 128, 128);
    SecretKey tmp = skf.generateSecret(spec);
    SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance(method);

    if(method.contains("CBC")) {
      byte[] ivByte = new byte[cipher.getBlockSize()];
      IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);

      cipher.init(mode, key, ivParamsSpec);
    }else{
      cipher.init(mode, key);
    }

    return cipher;
  }
}
