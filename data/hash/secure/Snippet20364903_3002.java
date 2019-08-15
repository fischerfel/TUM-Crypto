import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.jersey.core.util.Base64;

public class CipherUtils
{
private static Cipher cipher;
private static SecretKeySpec key;
private static AlgorithmParameterSpec spec;
public static final String SEED_16_CHARACTER = "hello";

public CipherUtils() throws Exception {
    // hash password with SHA-256 and crop the output to 128-bit for key
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(SEED_16_CHARACTER.getBytes("UTF-8"));
    byte[] keyBytes = new byte[16];
    System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    key = new SecretKeySpec(keyBytes, "AES");
    spec = getIV();
}

public AlgorithmParameterSpec getIV() {
    byte[] iv = { 0x00, 0x50, 0x00, 0x00, 0x00, 0x00, 0x72, 0x00, 0x00, 0x00, 0x46, 0x00, 0x23, 0x00, 0x00, 0x00 };

    IvParameterSpec ivParameterSpec;
    ivParameterSpec = new IvParameterSpec(iv);

    return ivParameterSpec;
}

public String encrypt(String plainText) throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
    String encryptedText = new String(Base64.encode(encrypted));

    return encryptedText;
}

public String decrypt(String cryptedText) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    byte[] bytes = Base64.decode(cryptedText);
    byte[] decrypted = cipher.doFinal(bytes);
    String decryptedText = new String(decrypted, "UTF-8");

    return decryptedText;
}
 }
