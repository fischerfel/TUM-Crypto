import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES {

    private static final int ITERATIONS = 120000;
    private static final int SALT_SIZE_IN_BYTES = 8;
    private static final String algorithm = "PBEWithSHA256And128BitAES-CBC-BC";
    private static final byte[] KEY_SALT = "a fixed key salt".getBytes(Charset.forName("UTF-8"));

    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private SecretKey key;
    private RandomGenerator randomGenerator = new RandomGenerator();

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
            Security.addProvider(new BouncyCastleProvider());
    }

    public AES(String passphrase) throws Exception {
        encryptCipher = Cipher.getInstance(algorithm);
        decryptCipher = Cipher.getInstance(algorithm);
        PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), KEY_SALT, ITERATIONS);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        key = keyFactory.generateSecret(keySpec);
    }

    public byte[] encrypt(byte[] data) throws Exception {
        byte[] salt = randomGenerator.generateRandom(SALT_SIZE_IN_BYTES);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONS);
        data = DataUtil.append(data, salt);

        byte[] encrypted;
        synchronized (encryptCipher) {
            // as a security constrain, it is necessary to use different salts per encryption
            // core issue: want to avoid this reinitialization to change the salt that will be used. Its quite time consuming
            encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, parameterSpec);
            encrypted = encryptCipher.doFinal(data);
        }
        return DataUtil.append(encrypted, salt);
    }

    public byte[] decrypt(byte[] data) throws Exception {
        byte[] salt = extractSaltPart(data);
        data = extractDataPart(data);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONS);

        byte[] decrypted;

        synchronized (decryptCipher) {
            // as a security constrain, it is necessary to use different salts per encryption
            // core issue: want to avoid this reinitialization to change the salt that will be used. Its quite time consuming
            decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, parameterSpec); 
            decrypted = decryptCipher.doFinal(data);
        }

        byte[] decryptedSalt = extractSaltPart(decrypted);

        if (Arrays.equals(salt, decryptedSalt))
            return extractDataPart(decrypted);
        else
            throw new IllegalArgumentException("Encrypted data is corrupted: Bad Salt");
    }

    protected byte[] extractDataPart(byte[] bytes) {
        return DataUtil.extract(bytes, 0, bytes.length - SALT_SIZE_IN_BYTES);
    }

    protected byte[] extractSaltPart(byte[] bytes) {
        return DataUtil.extract(bytes, bytes.length - SALT_SIZE_IN_BYTES, SALT_SIZE_IN_BYTES);
    }

    // main method to basic check the code execution
    public static void main(String[] args) throws Exception {
        String plainText = "some plain text, have fun!";
        String passphrase = "this is a secret";

        byte[] data = plainText.getBytes(Charset.forName("UTF-8"));

        AES cipher = new AES(passphrase);
        byte[] encrypted = cipher.encrypt(data);
        byte[] decrypted = cipher.decrypt(encrypted);

        System.out.println("expected: true, actual: " + Arrays.equals(data, decrypted));
    }
}

// Utility class
class RandomGenerator {

    private SecureRandom random = new SecureRandom();

    public RandomGenerator() {
        random = new SecureRandom();
        random.nextBoolean();
    }

    public synchronized byte[] generateRandom(int length) {
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
}

// Utility class
class DataUtil {

    public static byte[] append(byte[] data, byte[] append) {
        byte[] merged = new byte[data.length + append.length];
        System.arraycopy(data, 0, merged, 0, data.length);
        System.arraycopy(append, 0, merged, data.length, append.length);
        return merged;
    }

    public static byte[] extract(byte[] data, int start, int length) {
        if (start + length > data.length)
            throw new IllegalArgumentException("Cannot extract " + length + " bytes starting from index " + start + " from data with length " + data.length);

        byte[] extracted = new byte[length];
        System.arraycopy(data, start, extracted, 0, length);
        return extracted;
    }

}
