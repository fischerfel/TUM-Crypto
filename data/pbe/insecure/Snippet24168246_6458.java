import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

public class EncryptInJava
{
    public static void main(String[] args)
    {
      String encryptionPassword = "q1w2e3r4t5y6";
      byte[] salt = { -128, 64, -32, 16, -8, 4, -2, 1 };
      int iterations = 50;

      try
      {
        KeySpec keySpec = new PBEKeySpec(encryptionPassword.toCharArray(), salt, iterations);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);

        Cipher encoder = Cipher.getInstance(key.getAlgorithm());
        encoder.init(Cipher.ENCRYPT_MODE, key, paramSpec);

        String str_to_encrypt = "MyP455w0rd";
        byte[] enc = encoder.doFinal(str_to_encrypt.getBytes("UTF8"));

        System.out.println("encrypted = " + DatatypeConverter.printBase64Binary(enc));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
}
