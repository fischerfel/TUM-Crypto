package chat;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class DesEncrypter {
  Cipher ecipher;
  Cipher dcipher;
  byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35,
          (byte) 0xE3, (byte) 0x03 };
  public DesEncrypter(String passPhrase) throws Exception {
    int iterationCount = 2;
    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
    SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
    ecipher = Cipher.getInstance(key.getAlgorithm());
    dcipher = Cipher.getInstance(key.getAlgorithm());
    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
  }
  public String encrypt(String str) throws Exception {
    return new BASE64Encoder().encode(ecipher.doFinal(str.getBytes()));
  }
  public String decrypt(String str) throws Exception {
    return new String(dcipher.doFinal(new BASE64Decoder().decodeBuffer(str)));
  }
}
