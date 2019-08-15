import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class CipherUtil {
    private static Logger log = Logger.getLogger(CipherUtil.class);
    private static final String SECRET_KEY = "000102030405060708090A0B0C0D0E0F";
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;

    private static CipherUtil cipherUtil;

    private CipherUtil() {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            log.error(ex);
        }
        byte[] key = null;
        try {
            key = Hex.decodeHex(SECRET_KEY.toCharArray());
        } catch (DecoderException ex) {
            log.error(ex);
        }
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    public static synchronized CipherUtil getCipherUtilObject() {
        if (cipherUtil == null) {
            cipherUtil = new CipherUtil();
        }
        return cipherUtil;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public String encrypt(String plainText) {
        if (plainText == null)
            return null;
        String encryptedText = null;
        byte[] encrypted = null;

        synchronized (cipher) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } catch (InvalidKeyException e) {
                log.error(e.getMessage());
            }
        }

        synchronized (cipher) {
            try {
                encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
                encryptedText = new String(Base64.encodeBase64(encrypted));
            } catch (IllegalBlockSizeException | BadPaddingException
                    | UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }

        return encryptedText;
    }

    public synchronized String decrypt(String encryptedText) {
        if (encryptedText == null)
            return null;
        byte[] toDecrypt = null;
        byte[] original = null;
        String decryptedText = null;

        synchronized (cipher) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            } catch (InvalidKeyException e) {
                log.error(e.getMessage());
            }
        }
        toDecrypt = Base64.decodeBase64(encryptedText);
        synchronized (cipher) {
            try {
                original = cipher.doFinal(toDecrypt);
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                log.error(e.getMessage());
            }
        }
        try {
            decryptedText = new String(original, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

        return decryptedText;
    }
}
