import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AesEncryptionTest {
    static IvParameterSpec initialisationVector = generateInitialisationVector();
    static SecretKey encryptionKey = generateKey();
    static String plainText = "test text 123\0\0\0";

    public static void main(String [] args) {
        try {
            System.out.println("Initial Plain Text = " + plainText);

            byte[] encryptedText = encrypt(plainText, encryptionKey);
            System.out.println("Encrypted Text     = " + encryptedText);

            String decryptedText = decrypt(encryptedText, encryptionKey);
            System.out.println("Decrypted Text     = " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String plainText, SecretKey encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, initialisationVector);
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] encryptedText, SecretKey encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, initialisationVector);
        return new String(cipher.doFinal(encryptedText),"UTF-8");
    }

    public static SecretKey generateKey() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException ex) {
           // Whine a little
        }
        return secretKey;
    }

    public static IvParameterSpec generateInitialisationVector() {
        byte[] initVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initVector);

        return new IvParameterSpec(initVector);
   }
}
