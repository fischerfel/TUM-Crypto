import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import android.util.Base64

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncryptDecryptTest {

  public static void main(String[] args) throws Exception {
    String key = "12345678";
    String ciphertext = encrypt(key, "foo");

    String decrypted = decrypt(key, ciphertext.trim());
    String encrypted = encrypt(key, decrypted.trim());

    if (ciphertext.contentEquals(encrypted.trim())) {
      System.out.println("decrypted!");
    } else {
      System.out.println("wrong key!");
    }
  }

  public static String encrypt(String key, String data)
      throws GeneralSecurityException, UnsupportedEncodingException {
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF8"));
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
    byte[] dataBytes = data.getBytes("UTF8");
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    return Base64.encodeToString(cipher.doFinal(dataBytes), Base64.DEFAULT);
  }

  public static String decrypt(String key, String data)
      throws GeneralSecurityException, UnsupportedEncodingException {
    byte[] dataBytes = Base64.decode(data, Base64.DEFAULT);
    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF8"));
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] dataBytesDecrypted = (cipher.doFinal(dataBytes));
    return new String(dataBytesDecrypted);
  }    
}
