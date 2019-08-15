import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESWithStaticKeyAndRandomIV {

    private static final String KEY_ALIAS = "secret";

    private static byte[] KEY = new byte[] { (byte) 0x14, (byte) 0x0b,
            (byte) 0x41, (byte) 0xb2, (byte) 0x2a, (byte) 0x29, (byte) 0xbe,
            (byte) 0xb4, (byte) 0x06, (byte) 0x1b, (byte) 0xda, (byte) 0x66,
            (byte) 0xb6, (byte) 0x74, (byte) 0x7e, (byte) 0x14 };

    private static ProtectionParameter PASSWORD = new KeyStore.PasswordProtection(
            new char[] {'p', 'a', 's', 's', 'w', 'o', 'r', 'd'});

    private final KeyStore store;

    private AESWithStaticKeyAndRandomIV(KeyStore store) {
        this.store = store;
    }

    public byte[] encrypt(byte[] plaintext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key;
            try {
                key = ((SecretKeyEntry) store.getEntry(KEY_ALIAS, PASSWORD))
                        .getSecretKey();
            } catch (UnrecoverableEntryException | KeyStoreException e) {
                throw new IllegalStateException("What key?", e);
            }
            cipher.init(Cipher.ENCRYPT_MODE, key);

            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

            byte[] ciphertext = new byte[iv.length
                    + cipher.getOutputSize(plaintext.length)];
            System.arraycopy(iv, 0, ciphertext, 0, iv.length);
            cipher.doFinal(plaintext, 0, plaintext.length, ciphertext,
                    iv.length);
            return ciphertext;
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidParameterSpecException
                | ShortBufferException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new IllegalStateException(
                    "CBC encryption with standard algorithm should never fail",
                    e);
        }
    }

    public byte[] decrypt(byte[] ciphertext) throws IllegalBlockSizeException,
            BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key;
            try {
                key = ((SecretKeyEntry) store.getEntry(KEY_ALIAS, PASSWORD))
                        .getSecretKey();
            } catch (UnrecoverableEntryException | KeyStoreException e) {
                throw new IllegalStateException("What key?", e);
            }

            if (ciphertext.length < cipher.getBlockSize()) {
                throw new IllegalArgumentException(
                        "Ciphertext too small to contain IV");
            }

            IvParameterSpec ivSpec = new IvParameterSpec(ciphertext, 0,
                    cipher.getBlockSize());
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

            byte[] plaintext = new byte[cipher.getOutputSize(ciphertext.length
                    - cipher.getBlockSize())];
            cipher.doFinal(ciphertext, cipher.getBlockSize(), ciphertext.length
                    - cipher.getBlockSize(), plaintext, 0);
            return plaintext;
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | ShortBufferException
                | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(
                    "CBC decryption with standard algorithm should be available",
                    e);
        }
    }

    public static KeyStore createStoreWithSecretKey() {

        try {
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            keyStore.load(null);
            SecretKey key = new SecretKeySpec(KEY, "AES");
            keyStore.setEntry(KEY_ALIAS, new KeyStore.SecretKeyEntry(key), PASSWORD);
            return keyStore;
        } catch (KeyStoreException | NoSuchAlgorithmException
                | CertificateException | IOException e) {
            throw new IllegalStateException("Unable to create key store", e);
        }
    }

    public static void main(String[] args) throws Exception {
        AESWithStaticKeyAndRandomIV crypt = new AESWithStaticKeyAndRandomIV(
                createStoreWithSecretKey());

        byte[] plaintext = crypt.decrypt(crypt.encrypt("owlstead"
                .getBytes(StandardCharsets.UTF_8)));
        System.out.println(new String(plaintext, StandardCharsets.UTF_8));
    }
}
