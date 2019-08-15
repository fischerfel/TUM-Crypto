import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class BouncyCastleEX {

private static final int iterations = 2000;
private static final int keyLength = 256;
private static final SecureRandom random = new SecureRandom();
private static BouncyCastleEX instance = null;

public String encryptString(String plaintext, String passphrase, String salt)
        throws Exception {
    return Base64.encode(encrypt(plaintext, passphrase, salt));
}

public String decryptString(String encrypted, String passphrase, String salt)
        throws Exception {
    return decrypt(Base64.decode(encrypted), passphrase, salt);
}

private BouncyCastleEX() {
    Security.addProvider(new BouncyCastleProvider());
}

public static BouncyCastleEX getInstance() {
    if (instance == null) {
        instance = new BouncyCastleEX();
    }
    return instance;
}

private byte[] encrypt(String plaintext, String passphrase, String salt)
        throws Exception {
    SecretKey key = generateKey(passphrase, salt);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    byte[] ivBytes = generateIVBytes(cipher);
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes),
            random);
    return Arrays
            .concatenate(ivBytes, cipher.doFinal(plaintext.getBytes()));
}

private String decrypt(byte[] encrypted, String passphrase, String salt)
        throws Exception {
    SecretKey key = generateKey(passphrase, salt);
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, key,
            new IvParameterSpec(Arrays.copyOfRange(encrypted, 0, 12)),
            random);
    return new String(cipher.doFinal(Arrays.copyOfRange(encrypted, 12,
            encrypted.length)));
}

private SecretKey generateKey(String passphrase, String salt)
        throws Exception {
    PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(),
            salt.getBytes(), iterations, keyLength);
    SecretKeyFactory keyFactory = SecretKeyFactory
            .getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
    return keyFactory.generateSecret(keySpec);
}

private byte[] generateIVBytes(Cipher cipher) throws Exception {
    byte[] ivBytes = new byte[12];
    random.nextBytes(ivBytes);

    return ivBytes;
}

}
