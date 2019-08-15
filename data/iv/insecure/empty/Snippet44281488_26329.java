import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AESDecryption {
private static String key = "1234567890123456";
private static String encryptedStr = "0aZdRxsIqSpFtuszNr73na/J9JuMLNB0J6T2f2FrV0sUlMmbW4prbZMmXGnLU4W6CDlb5F1lb8js\r\nRHw6tfyZd5ZL//ZUlozE916wvP+zd+uUfjpk2Bl9o2uAu+1bsNoAVdtP5m5fbnkjxf9yLRzREVVO\r\nIwYQOxNI/CeX2dzF/Uc=";
private static String padding = "AES/CBC/PKCS5Padding";
private static int iterationCount = 65536;
private static int keyLength = 128;
private static String secretKeyAlg = "PBEWithHmacSHA1AndAES_128";

public static void main(String[] args) throws Exception {
    String finalStrDec = null;
    SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyAlg);
    PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), generateSalt(), iterationCount, keyLength);
    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

    IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
    Cipher cipherDec = Cipher.getInstance(padding);
    cipherDec.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
    byte[] original = cipherDec.doFinal(Base64.decodeBase64(encryptedStr));
    finalStrDec = new String(original);
    System.out.println(finalStrDec);
}

public static byte[] generateSalt() throws UnsupportedEncodingException {
    SecureRandom random = new SecureRandom();
    byte bytes[] = new byte[20];
    random.nextBytes(bytes);
    String salt = new String(bytes);
    return salt.getBytes("UTF-8");
}
}
