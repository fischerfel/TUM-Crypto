import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class Main
{

    private static class DesEncrypter
    {
        /** algorithm used for encrpytion and decryption */
        private static final String ALGORITHM = "PBEWithMD5AndDES";

        /** 8-byte Salt. */
        private static final byte[] SALT = { <You salt> };

        /** Iteration count. */
        private static final int ITERATION_COUNT = 19;

        /** Stores parameter specification. */
        private static final AlgorithmParameterSpec PARAM_SPEC = new PBEParameterSpec(
            SALT, ITERATION_COUNT);

        /** Key specification. */
        private final KeySpec keySpec;

        /** Secret key. */
        private final SecretKey key;

        public DesEncrypter(final String passPhrase)
        {
            // Create the key
            keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT);
            try
            {
                key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
            }
            catch (final Exception ex)
            {
                throw new RuntimeException("Could not create DesEncrypter: " + ex.getMessage(), ex);
            }
        }

        public final String encrypt(final String message)
        {
            try
            {
                // Create cipher instance
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                // Initialize cipher
                cipher.init(Cipher.ENCRYPT_MODE, key, PARAM_SPEC);
                // Encode string
                final byte[] enc = cipher.doFinal(message.getBytes("UTF8"));
                // Encode bytes to base64 to get a string
                return Base64.encodeBase64String(enc);
            }
            catch (final Exception ex)
            {
                throw new RuntimeException("Error encrypting message.", ex);
            }
        }
    }

    public static void main(final String[] args)
    {
        if (args.length == 2)
        {
            System.out.println("!!" + new DesEncrypter(args[0]).encrypt(args[1]));
        }
    }
}
