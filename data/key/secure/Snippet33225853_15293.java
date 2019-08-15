package nl.owlstead.stackoverflow;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class VeryStaticStringEncryption {

    private static final String KEY_FILE = "aes_key.key";
    private static final SecretKey AES_KEY = retrieveSecretKey(KEY_FILE);

    private VeryStaticStringEncryption() {
        // avoid instantiation
    }

    public static String encrypt(final String text) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int n = cipher.getBlockSize();

            final SecureRandom random = new SecureRandom();
            final byte[] iv = new byte[n];
            random.nextBytes(iv);
            final IvParameterSpec ivspec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, AES_KEY, ivspec);

            final byte[] plaintext = text.getBytes(UTF_8);
            byte[] ciphertext = Arrays.copyOf(iv,
                    n + cipher.getOutputSize(plaintext.length));
            final int ciphertextSize = cipher.doFinal(
                    plaintext, 0, plaintext.length,
                    ciphertext, n);

            // output size may be bigger, but not likely
            if (n + ciphertextSize  < ciphertext.length) {
                ciphertext = Arrays.copyOf(ciphertext, n + ciphertextSize);
            }

            return Base64.getUrlEncoder().encodeToString(ciphertext);
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException("Could not encrypt string", e);
        }
    }

    public static String decrypt(final String text) {
        try {
            // throws IllegalArgumentException if decoding fails
            final byte[] ciphertext = Base64.getUrlDecoder().decode(text);

            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int n = cipher.getBlockSize();

            // CBC specific
            if (ciphertext.length < n + n || ciphertext.length % n != 0) {
                throw new IllegalArgumentException("Ciphertext has incorrect size");
            }

            final IvParameterSpec ivspec = new IvParameterSpec(ciphertext, 0, n);
            cipher.init(Cipher.DECRYPT_MODE, AES_KEY, ivspec);
            final byte[] plaintext = cipher.doFinal(ciphertext, n, ciphertext.length - n);
            return new String(plaintext, UTF_8);
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException("Could not encrypt string", e);
        }
    }

    public static SecretKey retrieveSecretKey(final String keyResource) {
        try (final InputStream fis = VeryStaticStringEncryption.class.getResourceAsStream(keyResource)) {
            final byte[] rawkey = new byte[16];
            for (int i = 0; i < 16; i++) {
                final int b = fis.read();
                if (b == -1) {
                    throw new IOException("Key is not 16 bytes");
                }
                rawkey[i] = (byte) b;
            }

            if (fis.read() != -1) {
                throw new IOException("Key is not 16 bytes");
            }

            return new SecretKeySpec(rawkey, "AES");
        } catch (final IOException e) {
            // e may contain confidential information
            throw new IllegalStateException("AES key resource not available");
        }
    }

    public static void main(String[] args) {
        String ct = encrypt("owlstead");
        System.out.println(ct);
        String pt = decrypt(ct);
        System.out.println(pt);
    }
}
