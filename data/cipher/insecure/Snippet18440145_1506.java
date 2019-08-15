import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Crypto {
    public SecretKeySpec generateSecretKey(String password) throws Exception {
        MessageDigest shahash = MessageDigest.getInstance("SHA-1");
        byte[] key = shahash.digest();
        key = Arrays.copyOf(key,  16);
        return new SecretKeySpec(key, "AES");
    }

    public byte[] encrypt(String text, SecretKeySpec secretkey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretkey);
        return cipher.doFinal(text.getBytes());
    }

    public byte[] decrypt(byte[] encryptedtext, SecretKeySpec secretkey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretkey);
        return cipher.doFinal(encryptedtext);
    }
}
