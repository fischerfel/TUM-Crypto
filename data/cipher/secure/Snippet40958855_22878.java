import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Rsa {

    static KeyPair generateKeyPair() throws GeneralSecurityException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(4096);
        return gen.generateKeyPair();
    }

    static byte[] encrypt(byte[] plaintext, PublicKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    static byte[] decrypt(byte[] ciphertext, PrivateKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }

    public static void main(String[] args) throws GeneralSecurityException {
        KeyPair keys = generateKeyPair();
        byte[] plaintext = "hello, world".getBytes(StandardCharsets.UTF_8);
        byte[] ciphertext = encrypt(plaintext, keys.getPublic());
        byte[] plainAgain = decrypt(ciphertext, keys.getPrivate());
        System.out.println(new String(plainAgain, StandardCharsets.UTF_8));
    }
}
