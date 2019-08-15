import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class CryptExample {

    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String UTF_8_CHARSET = "UTF-8";
    private static final int BASE_64_ENCODING = Base64.DEFAULT;

    private static final String STR_PASSWORD = "bM4uOGs600okBDsF";
    private static final String STR_SALT = "iMYGYBFpl2ghOy1k0wMb";
    private static final int ITERATION_COUNT = 4;
    private static final int KEY_SIZE = 128;

    private static CryptExample mCrypt;

    private char[] mPasswordArray;
    private byte[] mSalt;
    private SecretKeySpec mSecretKeySpec;
    private Cipher mCipher;

    public static CryptExample getInstance() {
        if (mCrypt == null) {
            mCrypt = new CryptExample();
        }

        return mCrypt;
    }

    private CryptExample() {

        try {

            mPasswordArray = STR_PASSWORD.toCharArray();
            mSalt = STR_SALT.getBytes(UTF_8_CHARSET);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            init();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }

    }

    private void init() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidParameterSpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        PBEKeySpec spec = new PBEKeySpec(mPasswordArray, mSalt, ITERATION_COUNT, KEY_SIZE);

        SecretKey secretKey = factory.generateSecret(spec);
        mSecretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        mCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

    }

    public String encrypt(String szContent) throws NoSuchAlgorithmException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec);

        byte[] encryptedContentBytes = mCipher.doFinal(szContent.getBytes(UTF_8_CHARSET));

        return Base64.encodeToString(encryptedContentBytes, BASE_64_ENCODING);
    }

    public String decrypt(String szEncryptedContent) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidParameterSpecException {

        byte[] encryptedContentBytes = Base64.decode(szEncryptedContent, BASE_64_ENCODING);

        mCipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec);

        byte[] decryptedContentBytes = mCipher.doFinal(encryptedContentBytes);

        return new String(decryptedContentBytes, UTF_8_CHARSET);
    }

}
