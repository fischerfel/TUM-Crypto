import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2ForPasswordHash {

    private static final String PBKDF_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 10_000;
    // should be less than the size of the underlying hash
    private static final int PASSWORD_HASH_SIZE_BYTES = 16;
    private static final int SALT_SIZE_BYTES = 16;

    public static byte[] generateRandomSalt(final int saltSizeBytes) {
        final SecureRandom rng = new SecureRandom();
        final byte[] salt = new byte[saltSizeBytes];
        rng.nextBytes(salt);
        return salt;
    }

    public static byte[] generatePasswordHash(final char[] password,
            final byte[] salt) {
        SecretKeyFactory f;
        try {
            f = SecretKeyFactory.getInstance(PBKDF_ALGORITHM);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("PBKDF algorithm "
                    + PBKDF_ALGORITHM + " not available", e);
        }
        final KeySpec ks = new PBEKeySpec(password, salt, ITERATION_COUNT,
                PASSWORD_HASH_SIZE_BYTES * Byte.SIZE);
        SecretKey s;
        try {
            s = f.generateSecret(ks);
        } catch (final InvalidKeySpecException e) {
            throw new IllegalArgumentException(
                    "PBEKeySpec should always be valid for " + PBKDF_ALGORITHM,
                    e);
        }
        return s.getEncoded();
    }

    public static final String toHex(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02x", data[i]));
        }
        return sb.toString();
    }

    public static void main(final String[] args) throws Exception {
        final char[] password = { 'o', 'w', 'l' };
        final byte[] salt = generateRandomSalt(SALT_SIZE_BYTES);
        System.out.println(toHex(salt));
        final byte[] hash = generatePasswordHash(password, salt);
        System.out.println(toHex(hash));
    }
}
