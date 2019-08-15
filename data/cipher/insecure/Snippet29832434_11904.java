import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PwdCipher {

    private static final Logger LOG = LoggerFactory.getLogger(PwdCipher.class);

    private static final int BASE64_ARG0 = 32;

    private static final String SECRET = "tvnw63ufg9gh5392";

    private static byte[] linebreak = {};
    private static SecretKey key;
    private static Cipher cipher;
    private static Base64 coder;

    static {
            key = new SecretKeySpec(SECRET.getBytes(), "AES");
            try {
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
                LOG.debug("Erro ao criar encriptador.", e);
            }
            coder = new Base64(BASE64_ARG0, linebreak, true);
    }

    private PwdCipher(){
    }

    public static synchronized String encrypt(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return new String(coder.encode(cipherText));
        } catch (Exception e) {
            throw new GdocException("Erro ao encriptar senha.", e);
        }
    }

    public static synchronized String decrypt(String codedText) {
        try {
            byte[] encypted = coder.decode(codedText.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(encypted);
            return new String(decrypted);
        } catch (Exception e) {
            throw new GdocException("Erro ao decriptar senha.", e);
        }
    }

}
