import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class apiKeyGenerate {
  public static void main(String[] args) throws Exception {
   // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    byte[] input = "input".getBytes();
    byte[] ivBytes = "1234567812345678".getBytes();

    Cipher cipher =  Cipher.getInstance("DES/CBC/PKCS5Padding");
    KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
    generator.init(128);
    Key encryptionKey = generator.generateKey();
    System.out.println("key : " + new String(encryptionKey.getEncoded()));
   }
}
