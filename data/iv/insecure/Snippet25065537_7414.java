import static java.nio.charset.StandardCharsets.UTF_16LE;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class AuthenticationStringDecrypter {

    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
    private static final int KEY_SIZE = 256;

    public static void main(final String[] args) throws Exception {
        System.out.println(decryptAuthorizationString(
                "c1W2YO1vYQzu6czteEidrG0U4g5gT4h57vAlP7tdjcY=", "GAT"));
    }

    private static String decryptAuthorizationString(final String authString,
            final String password) {
        try {
            // --- check if AES-256 is available
            if (Cipher.getMaxAllowedKeyLength(AES_CBC_PKCS5PADDING) < KEY_SIZE) {
                throw new IllegalStateException("Unlimited crypto files not present in this JRE");
            }

            // --- create cipher
            final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);

            // --- create the key and initial vector bytes
            final byte[] passwordEncoded = password.getBytes(UTF_16LE);
            final byte[] keyData = Arrays.copyOf(passwordEncoded, KEY_SIZE
                    / Byte.SIZE);
            final byte[] ivBytes = Arrays.copyOf(keyData, cipher.getBlockSize());

            // --- init cipher
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyData, "AES"),
                    new IvParameterSpec(ivBytes));

            // --- decode & decrypt authentication string
            final byte[] authBytes = Base64.decode(authString);
            final byte[] decryptedData = cipher.doFinal(authBytes);

            // WARNING: may still decrypt to wrong string if
            // authString or password are incorrect - 
            // BadPaddingException may *not* be thrown
            return new String(decryptedData, UTF_16LE);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // failure to authenticate
            return null;
        } catch (final GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Algorithms or unlimited crypto files not available", e);
        }
    }
}
