import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptedPropertyPlacementConfigurer extends PropertyPlaceholderConfigurer
{
    /** algorithm used for encrpytion and decryption */
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    /** 8-byte Salt. */
    private static final byte[] SALT = { ... };

    /** Iteration count. */
    private static final int ITERATION_COUNT = 19;

    /** Stores parameter specification. */
    private static final AlgorithmParameterSpec PARAM_SPEC = new PBEParameterSpec(SALT, ITERATION_COUNT);

    //All properties starting with !! will be decrypted.
    private static final String ENCRYPTIGION_LEADIN = "!!";

    public static class EncrypterException extends RuntimeException
    {
        private static final long serialVersionUID = -7336009350594115318L;

        public EncrypterException(final String message, final Throwable cause)
        {
            super(message, cause);
        }

        public EncrypterException(final String message)
        {
            super(message);
        }
    }

    private static String decrypt(final String passPhrase, final String message)
    {
        // Create the key
        final KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT);
        SecretKey key;
        try
        {
            key = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(keySpec);
        }
        catch (final Exception e)
        {
            throw new EncrypterException("Error setting up encryption details.", e);
        }

        if (!Base64.isBase64(message))
        {
            throw new EncrypterException("Message is not a valid base64 message.");
        }

        final String result;
        try
        {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, key, PARAM_SPEC);

            final byte[] dec = Base64.decodeBase64(message);

            result = new String(cipher.doFinal(dec), "UTF-8");
        }
        catch (final Exception e)
        {
            throw new EncrypterException("Error decrypting content.", e);
        }

        return result;
    }

    @Override
    protected String convertPropertyValue(final String originalValue)
    {
        if (StringUtils.isNotBlank(originalValue) && originalValue.startsWith(ENCRYPTIGION_LEADIN))
        {
            return decrypt("<Your magic password>", originalValue.substring(2));
        }
        return super.convertPropertyValue(originalValue);
    }

}
