import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class Crypto {

    private final static String CIPHER_ALGORITHM = "AES";
    private final static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public final static int CRYPTO_KEY_SIZE = 16;    

    public static byte[] encryptByteArray(byte[] blockToEncrypt, int maxLengthToEncrypt, byte[] encryptionKey, byte[] ivBytes) {
        return processCipher(blockToEncrypt, maxLengthToEncrypt, Cipher.ENCRYPT_MODE, ivBytes, encryptionKey);
    }

    public static byte[] decryptByteArray(byte[] encryptedData, byte[] encryptionKey, byte[] ivBytes) {
        return processCipher(encryptedData, encryptedData.length, Cipher.DECRYPT_MODE, ivBytes, encryptionKey);
    }

    private static byte[] processCipher(byte[] blockToEncrypt, int maxLength, int cryptionMode, byte[] ivBytes, byte[] encryptionKey) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            final Cipher cipher = initCipher(cryptionMode, iv, encryptionKey);
            return cipher.doFinal(blockToEncrypt, 0, maxLength);
        } catch (Exception e) {
            throw new RuntimeException("Failure", e);
        }
    }

    private static Cipher initCipher(int cryptionMode, IvParameterSpec iv, byte[] encryptionKey) {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(CIPHER_ALGORITHM);

            final SecureRandom randomSeed = new SecureRandom();
            randomSeed.setSeed(encryptionKey);
            keyGen.init(CRYPTO_KEY_SIZE * 8, randomSeed);

            // Generate the secret key specs.
            final SecretKey secretKey = keyGen.generateKey();

            final SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), CIPHER_ALGORITHM);

            // Instantiate the cipher
            final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

            cipher.init(cryptionMode, secretKeySpec, iv);
            return cipher;

        } catch (Exception e) {
            throw new RuntimeException("Failure", e);
        }
    }
}
