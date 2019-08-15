import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author alvaro
 */
public class Encriptor {

    // Constants -----------------------------------------------------

    private static final String PASS_PHRASE = "12345678901234";//says wrong length
    private static final String SALT_VALUE = "123456";
    private static final int PASSWORD_ITERATIONS = 1;
    private static final String INIT_VECTOR = "1234567890123456";
    private static final int KEY_SIZE = 128;

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    // Public --------------------------------------------------------

    public String encrypt(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        MessageDigest digest = MessageDigest.getInstance(DIGEST);
        digest.update(SALT_VALUE.getBytes());
        byte[] bytes = digest.digest(PASS_PHRASE.getBytes(ENCODING));
        SecretKey password = new SecretKeySpec(bytes, "AES");

        //Initialize objects
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] IV = INIT_VECTOR.getBytes();
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, password, ivParamSpec);
        byte[] encryptedData = cipher.doFinal(text.getBytes(ENCODING));
        return new BASE64Encoder().encode(encryptedData).replaceAll("\n", "");
    }

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------

}
