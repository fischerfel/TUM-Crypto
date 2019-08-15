import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class CounterIVCreator {

    private final int blockSizeBytes;
    private final byte[] ivCounter;

    public CounterIVCreator(final int blockSizeBytes) {
        if (blockSizeBytes % 2 != 0 || blockSizeBytes < 16) {
            // AKA don't use DES or 3DES
            throw new IllegalArgumentException("Block size should be even and at least 16 bytes");
        }

        this.blockSizeBytes = blockSizeBytes;
        this.ivCounter = new byte[blockSizeBytes / 2];
    }

    public CounterIVCreator(final byte[] oldCounter) {
        if (oldCounter.length < 8) {
            // AKA don't use DES or 3DES
            throw new IllegalArgumentException("Counter should be larger than 8 bytes");
        }

        this.blockSizeBytes = oldCounter.length * 2;
        this.ivCounter = oldCounter.clone();
    }


    public IvParameterSpec createIV() {
        increaseCounter(ivCounter);
        final byte[] iv = Arrays.copyOf(ivCounter, blockSizeBytes);
        return new IvParameterSpec(iv);
    }

    public byte[] getCounter() {
        return ivCounter.clone();
    }

    private static void increaseCounter(final byte[] counter) {
        for (int i = counter.length - 1; i >= 0; i--) {
            counter[i]++;
            if (counter[i] != 0) {
                break;
            }
        }
    }

    public static void main(final String ... args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        byte[] oldCounter;

        Cipher gcm = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec aesKey = new SecretKeySpec(new byte[Cipher.getMaxAllowedKeyLength("AES/GCM/NoPadding") / Byte.SIZE], "AES");

        {
            CounterIVCreator ivCreator = new CounterIVCreator(gcm.getBlockSize());
            IvParameterSpec ivSpec = ivCreator.createIV();
            gcm.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            gcm.updateAAD(ivSpec.getIV());
            byte[] ciphertext = gcm.doFinal("owlstead".getBytes(StandardCharsets.UTF_8));
            System.out.println(Hex.toHexString(ciphertext));
            gcm.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
            gcm.updateAAD(ivSpec.getIV());
            byte[] plaintext = gcm.doFinal(ciphertext);
            System.out.println(new String(plaintext, StandardCharsets.UTF_8));

            oldCounter = ivCreator.getCounter();
        }

        // part deux, creates an entirely different ciphertext
        {
            CounterIVCreator ivCreator = new CounterIVCreator(oldCounter);
            IvParameterSpec ivSpec = ivCreator.createIV();
            gcm.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            gcm.updateAAD(ivSpec.getIV());
            byte[] ciphertext = gcm.doFinal("owlstead".getBytes(StandardCharsets.UTF_8));
            System.out.println(Hex.toHexString(ciphertext));
            gcm.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
            gcm.updateAAD(ivSpec.getIV());
            byte[] plaintext = gcm.doFinal(ciphertext);
            System.out.println(new String(plaintext, StandardCharsets.UTF_8));
        }        
    }
}
