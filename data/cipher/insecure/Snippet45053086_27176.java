import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import sun.misc.BASE64Decoder;

public class EncryptionUtils
{
  private Cipher decryptCipher;
  private BASE64Decoder decoder = new BASE64Decoder();

  public EncryptionUtils() throws SecurityException {
    Security.addProvider(new com.sun.crypto.provider.SunJCE());

    char[] pass = "edurixkey".toCharArray();
    byte[] salt = {
      -93, 33, 36, 44, 
      -14, -46, 62, 25 };

    int iterations = 3;

    init(pass, salt, iterations);
  }

  public void init(char[] pass, byte[] salt, int iterations) throws SecurityException {
    try {
      PBEParameterSpec ps = new PBEParameterSpec(salt, 20);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

      decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
      decryptCipher.init(2, k, ps);
    }
    catch (Exception e) {
      throw new SecurityException("Could not initialize Encryption: " + e.getMessage());
    }
  }

  public synchronized String decrypt(String str) throws SecurityException {
    try {
      byte[] dec = decoder.decodeBuffer(str);
      byte[] utf8 = decryptCipher.doFinal(dec);
      return new String(utf8, "UTF8");
    }
    catch (Exception e) {
      throw new SecurityException("Could not decrypt: " + e.getMessage());
    }
  }
}   
