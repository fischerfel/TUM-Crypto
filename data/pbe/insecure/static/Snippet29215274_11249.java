import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class CryptQuestion {

    private static final String ALGORITHM = "PBEWithHmacSHA512AndAES_256";
    private static final int ITERATIONS = 1000; // Aside: not sure what is a good number, here.

    public static void main(final String[] args) throws Exception {
        final String message = "This is the secret message... BOO!";
        System.out.println("Original : " + message);
        final byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);

        final String password = "some password";
        final byte[] salt = "would be random".getBytes(StandardCharsets.US_ASCII);

        // Create the Key
        final SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        final PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS);
        SecretKey key = factory.generateSecret(keySpec);

        // Build the encryption cipher.
        final Cipher cipherEncrypt = Cipher.getInstance(ALGORITHM);
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt!
        final byte[] ciphertext = cipherEncrypt.doFinal(messageBytes);
        final byte[] iv = cipherEncrypt.getIV();

        // Now for decryption... The receiving end will have as input:
        // * ciphertext
        // * IV
        // * password
        // * salt

        // We just re-use 'key' from above, since it will be identical.

        final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATIONS);
        final IvParameterSpec ivParamSpec = new IvParameterSpec(iv);

        // Build the decryption cipher.
        final Cipher cipherDecrypt = Cipher.getInstance(ALGORITHM);
        // PROBLEM: If I pass "ivParamSpec", I get "java.security.InvalidAlgorithmParameterException: Wrong parameter type: PBE expected"
        // Whereas if I pass pbeParamSpec, I get "java.security.InvalidAlgorithmParameterException: Missing parameter type: IV expected"
        // What to do?
        cipherDecrypt.init(
                Cipher.DECRYPT_MODE,
                key,
                ivParamSpec
                //pbeParamSpec
                );

        final String decrypted = new String(
                cipherDecrypt.doFinal(ciphertext),
                StandardCharsets.US_ASCII);
        System.out.println("Decrypted: " + decrypted);
    }
}
