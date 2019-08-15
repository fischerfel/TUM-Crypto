import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class PassphraseWrapRSA {
    private static KeyPair generateRSAKeyPair(final int size) {
        KeyPairGenerator kpgen;
        try {
            kpgen = KeyPairGenerator.getInstance("RSA");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException();
        }
        kpgen.initialize(size);
        return kpgen.generateKeyPair();
    }

    public static byte[] generateSalt() {
        final SecureRandom rng = new SecureRandom();
        final byte[] salt = new byte[16];
        rng.nextBytes(salt);
        return salt;
    }

    private static SecretKey deriveAESKey(final byte[] salt,
            final char[] password) {
        try {
            final SecretKeyFactory factory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            final KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
            final SecretKey keyWrapKey = factory.generateSecret(spec);
            final SecretKey secret = new SecretKeySpec(keyWrapKey.getEncoded(),
                    "AES");
            return secret;

        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] encryptRSAPrivateKey(final RSAPrivateKey rsaPrivateKey,
            final SecretKey aesKey) {
        try {
            final Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

            final SecureRandom ivGen = new SecureRandom();
            final byte[] iv = new byte[c.getBlockSize()];
            ivGen.nextBytes(iv);
            c.init(Cipher.WRAP_MODE, aesKey, new IvParameterSpec(iv));
            final byte[] wrappedKey = c.wrap(rsaPrivateKey);
            return concat(iv, wrappedKey);
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] wrapRSAPrivateKey(final String passphrase,
            final RSAPrivateKey rsaPrivateKey) {
        // --- generate salt
        final byte[] newSalt = generateSalt();

        // --- derive symmetric key from salt and password
        final SecretKey aesKey = deriveAESKey(newSalt,
                passphrase.toCharArray());


        final byte[] encryptedPrivate = encryptRSAPrivateKey(rsaPrivateKey, aesKey);
        final byte[] saltedAndEncryptedPrivate = concat(newSalt,
                encryptedPrivate);
        return saltedAndEncryptedPrivate;
    }

    private static RSAPrivateKey decryptRSAPrivateKey(final byte[] encryptedRSAPrivateKey,
            final SecretKey aesKey) throws InvalidKeyException {
        try {
            final Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

            int offset = 0;
            final byte[] iv = Arrays.copyOfRange(encryptedRSAPrivateKey, 0,
                    c.getBlockSize());
            offset += c.getBlockSize();

            c.init(Cipher.UNWRAP_MODE, aesKey, new IvParameterSpec(iv));
            final Key key = c.unwrap(Arrays.copyOfRange(encryptedRSAPrivateKey, offset,
                    encryptedRSAPrivateKey.length), "RSA", Cipher.PRIVATE_KEY);
            return (RSAPrivateKey) key;
        } catch (final InvalidKeyException e) {
            throw e;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public static RSAPrivateKey unwrapRSAPrivateKey(final String passphrase,
            final byte[] saltedAndEncryptedPrivate) throws InvalidKeyException {
        int offset = 0;
        final byte[] backSalt = Arrays.copyOfRange(saltedAndEncryptedPrivate,
                offset, 16);
        offset += 16;
        final SecretKey backAESKey = deriveAESKey(backSalt,
                passphrase.toCharArray());
        final byte[] backEncryptedPrivateKey = Arrays.copyOfRange(
                saltedAndEncryptedPrivate, offset,
                saltedAndEncryptedPrivate.length);
        final RSAPrivateKey decryptedPrivate = decryptRSAPrivateKey(
                backEncryptedPrivateKey, backAESKey);
        return decryptedPrivate;
    }

    public static RSAPublicKey decodeRSAPublicKey(
            final byte[] x509EncodedPUblicKey) throws InvalidKeySpecException {
        try {
            final KeyFactory rsaPublicKeyFactory = KeyFactory.getInstance("RSA");
            final PublicKey pubKey = rsaPublicKeyFactory
                    .generatePublic(new X509EncodedKeySpec(x509EncodedPUblicKey));
            return (RSAPublicKey) pubKey;
        } catch (final InvalidKeySpecException e) {
            throw e;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] encodeRSAPublicKey(final RSAPublicKey rsaPublicKey) {
        return rsaPublicKey.getEncoded();
    }

    private static byte[] concat(final byte[] a, final byte[] a2) {
        final byte[] result = new byte[a.length + a2.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(a2, 0, result, a.length, a2.length);
        return result;
    }

    public static void main(final String[] args) throws Exception {
        // --- not required for Java 8
        Security.addProvider(new BouncyCastleProvider());

        // --- setup key pair (generated in advance)
        final String passphrase = "owlstead";
        final KeyPair kp = generateRSAKeyPair(1024);
        final RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();

        // --- encode and wrap
        byte[] x509EncodedRSAPublicKey = encodeRSAPublicKey(rsaPublicKey);
        final byte[] saltedAndEncryptedPrivate = wrapRSAPrivateKey(
                passphrase, rsaPrivateKey);

        // --- decode and unwrap
        final RSAPublicKey retrievedRSAPublicKey = decodeRSAPublicKey(x509EncodedRSAPublicKey);
        final RSAPrivateKey retrievedRSAPrivateKey = unwrapRSAPrivateKey(passphrase,
                saltedAndEncryptedPrivate);

        // --- check result
        System.out.println(retrievedRSAPublicKey);
        System.out.println(retrievedRSAPrivateKey);
    }
}
