import static java.nio.charset.StandardCharsets.*;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SecurityHelperGCM {
    private static final int NONCE_SIZE = 8;
    private static final int TAG_SIZE = 12;
    // make sure that the hexadecimals represent a *truly random* byte array
    // (e.g. use SecureRandom)
    private final SecretKey STATIC_SECRET_KEY = new SecretKeySpec(
            hexDecode("66e517bb5fd7df840060aed7e8b58986"), "AES");
    private Cipher cipher;

    private static byte[] hexDecode(final String hex) {
        final byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return data;
    }

    public SecurityHelperGCM() {
        try {
            this.cipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomNonce(final byte[] nonceBuffer,
            final int offset, final int size) {
        final SecureRandom rng = new SecureRandom();
        final byte[] nonce = new byte[size];
        rng.nextBytes(nonce);
        System.arraycopy(nonce, 0, nonceBuffer, offset, size);
        clearArray(nonce);

        return offset + size;
    }

    private static void clearArray(final byte[] nonce) {
        // clean up...
        for (int i = 0; i < nonce.length; i++) {
            nonce[i] = 0;
        }
    }

    private static GCMParameterSpec generateGCMParametersFromNonce(
            final byte[] nonceBuffer, final int offset, final int size,
            final int blockSize) {
        final GCMParameterSpec gcmParameters = new GCMParameterSpec(TAG_SIZE
                * Byte.SIZE, nonceBuffer, offset, size);
        return gcmParameters;
    }

    public String encrypt(final String secret) {
        final byte[] plaintext = secret.getBytes(UTF_8);
        final byte[] nonceAndCiphertext = new byte[NONCE_SIZE
                + plaintext.length + TAG_SIZE];

        int offset = generateRandomNonce(nonceAndCiphertext, 0, NONCE_SIZE);
        final GCMParameterSpec nonceIV = generateGCMParametersFromNonce(
                nonceAndCiphertext, 0, NONCE_SIZE, this.cipher.getBlockSize());

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.STATIC_SECRET_KEY,
                    nonceIV);
            offset += this.cipher.doFinal(plaintext, 0, plaintext.length,
                    nonceAndCiphertext, offset);
            if (offset != nonceAndCiphertext.length) {
                throw new IllegalStateException(
                        "Something wrong during encryption");
            }
            // Java 8 contains java.util.Base64
            return DatatypeConverter.printBase64Binary(nonceAndCiphertext);
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Missing basic functionality from Java runtime", e);
        }
    }

    public String decrypt(final String encrypted) throws BadPaddingException {
        final byte[] nonceAndCiphertext = DatatypeConverter
                .parseBase64Binary(encrypted);
        final GCMParameterSpec nonceIV = generateGCMParametersFromNonce(
                nonceAndCiphertext, 0, NONCE_SIZE, this.cipher.getBlockSize());
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.STATIC_SECRET_KEY,
                    nonceIV);
            final byte[] plaintext = this.cipher.doFinal(nonceAndCiphertext,
                    NONCE_SIZE, nonceAndCiphertext.length - NONCE_SIZE);
            return new String(plaintext, UTF_8);
        } catch (final BadPaddingException e) {
            throw e;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Missing basic functionality from Java runtime", e);
        }
    }

    public static void main(final String[] args) {
        final String secret = "owlstead";
        final SecurityHelperGCM securityHelperGCM = new SecurityHelperGCM();
        final String ct = securityHelperGCM.encrypt(secret);
        String pt = null;
        try {
            pt = securityHelperGCM.decrypt(ct);
        } catch (BadPaddingException e) {
            System.out.println("Ciphertext tampered, take action!");
        }
        System.out.println(pt);
    }
}
