package nl.owlstead.stackoverflow;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CreateKeyAndIVForAES_CBC {

    public static SecretKey createKey(final String algorithm, final int keysize, final Optional<Provider> provider, final Optional<SecureRandom> rng) throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator;
        if (provider.isPresent()) {
            keyGenerator = KeyGenerator.getInstance(algorithm, provider.get());
        } else {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        }

        if (rng.isPresent()) {
            keyGenerator.init(keysize, rng.get());
        } else {
            // not really needed for the Sun provider which handles null OK
            keyGenerator.init(keysize);
        }

        return keyGenerator.generateKey();
    }

    public static IvParameterSpec createIV(final int ivSizeBytes, final Optional<SecureRandom> rng) {
        final byte[] iv = new byte[ivSizeBytes];
        final SecureRandom theRNG = rng.orElse(new SecureRandom());
        theRNG.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static IvParameterSpec readIV(final int ivSizeBytes, final InputStream is) throws IOException {
        final byte[] iv = new byte[ivSizeBytes];
        int offset = 0;
        while (offset < ivSizeBytes) {
            final int read = is.read(iv, offset, ivSizeBytes - offset);
            if (read == -1) {
                throw new IOException("Too few bytes for IV in input stream");
            }
            offset += read;
        }
        return new IvParameterSpec(iv);
    }

    public static void main(String[] args) throws Exception {
        final SecureRandom rng = new SecureRandom();
        // you somehow need to distribute this key
        final SecretKey aesKey = createKey("AES", 128, Optional.empty(), Optional.of(rng));
        final byte[] plaintext = "owlstead".getBytes(UTF_8);

        final byte[] ciphertext;
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            final Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec ivForCBC = createIV(aesCBC.getBlockSize(), Optional.of(rng));
            aesCBC.init(Cipher.ENCRYPT_MODE, aesKey, ivForCBC);

            baos.write(ivForCBC.getIV());

            try (final CipherOutputStream cos = new CipherOutputStream(baos, aesCBC)) {
                cos.write(plaintext);
            }

            ciphertext = baos.toByteArray();
        }

        final byte[] decrypted;
        {
            final ByteArrayInputStream bais = new ByteArrayInputStream(ciphertext);

            final Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final IvParameterSpec ivForCBC = readIV(aesCBC.getBlockSize(), bais);
            aesCBC.init(Cipher.DECRYPT_MODE, aesKey, ivForCBC);

            final byte[] buf = new byte[1_024];
            try (final CipherInputStream cis = new CipherInputStream(bais, aesCBC);
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int read;
                while ((read = cis.read(buf)) != -1) {
                    baos.write(buf, 0, read);
                }
                decrypted = baos.toByteArray();
            }
        }

        System.out.println(new String(decrypted, UTF_8));
    }
}
