import java.security.*;
import javax.crypto.*;

public class RCC4 {

    public static void main(String[] args) throws Exception {
        String plain = "testisperfect";
        Key key = RCC4.getKey();
        String encrypted = RCC4.encrypt(plain, key);
        String decrypted = RCC4.decrypt(encrypted, key);
        System.out.println(encrypted);
        System.out.println(decrypted);
    }

    private static String rc4(String plaintext, int mode, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(mode, key);
        return new String(cipher.doFinal(plaintext.getBytes()));
    }

    public static String encrypt(String plaintext, Key key) throws Exception {
        return rc4(plaintext, Cipher.ENCRYPT_MODE, key);
    }

    public static String decrypt(String ciphertext, Key key) throws Exception {
        return rc4(ciphertext, Cipher.DECRYPT_MODE, key);
    }

    public static Key getKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        SecureRandom sr = new SecureRandom();
        kg.init(128, sr);
        return kg.generateKey();
    }

}
