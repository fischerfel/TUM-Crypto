    package support.security;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public final static String FILE_KEY = "qwertyuiopo";
    private final String CIPHER_MODE_PADDING = "AES/CBC/PKCS7Padding";
    private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

    private final int HASH_ITERATIONS = 10000;
    private final int KEY_LENGTH = 128;
    private byte[] mSalt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF}; // must save this for next time we want the key

    private PBEKeySpec mKeySpec;

    private SecretKeyFactory mKeyFactory = null;
    private SecretKey mSKey = null;
    private SecretKeySpec mSKeySpec = null;

    private byte[] mIvParameter = {0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91};
    private IvParameterSpec mIvParameterSpec;

    public AES(String key) {
        mKeySpec = new PBEKeySpec(key.toCharArray(), mSalt, HASH_ITERATIONS, KEY_LENGTH);
        try {
            mKeyFactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            mSKey = mKeyFactory.generateSecret(mKeySpec);
        } catch (NoSuchAlgorithmException e) {
            //Log.getStackTraceString(e);
        } catch (InvalidKeySpecException e) {
           // Log.getStackTraceString(e);
        }
        byte[] skAsByteArray = mSKey.getEncoded();
        mSKeySpec = new SecretKeySpec(skAsByteArray, "AES");
        mIvParameterSpec = new IvParameterSpec(mIvParameter);
    }

    public String encrypt(byte[] plaintext) {
        byte[] _plaintext = encrypt(CIPHER_MODE_PADDING, mSKeySpec, mIvParameterSpec, plaintext);
        String base64_plaintext = Base64Encoder.encode(_plaintext);
        return base64_plaintext;
    }

    public String decrypt(String base64_plaintext) {
        byte[] s = Base64Decoder.decodeToBytes(base64_plaintext);
        String decrypted = null;
        try {
            byte[] decryptBytes = decrypt(CIPHER_MODE_PADDING, mSKeySpec, mIvParameterSpec, s);//add check null by zwl when 2017.1.8
            if (decryptBytes != null) {
                decrypted = new String(decryptBytes, HttpContext.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            //Log.getStackTraceString(e);
        }
        return decrypted;
    }

    public byte[] encryptToBytes(byte[] plaintext) {
        byte[] cipherText = encrypt(CIPHER_MODE_PADDING, mSKeySpec, mIvParameterSpec, plaintext);
        return cipherText;
    }

    public byte[] decryptToBytes(byte[] plaintext) {
        return decrypt(CIPHER_MODE_PADDING, mSKeySpec, mIvParameterSpec, plaintext);
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) {
        try {

            Security.addProvider(new BouncyCastleProvider());
            Key key = new SecretKeySpec(msg, CIPHER_MODE_PADDING);
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException e) {
            //Log.getStackTraceString(e);
        } catch (NoSuchPaddingException e) {
           // Log.getStackTraceString(e);
        } catch (InvalidKeyException e) {
            //Log.getStackTraceString(e);
        } catch (InvalidAlgorithmParameterException e) {
           // Log.getStackTraceString(e);
        } catch (IllegalBlockSizeException e) {
           // Log.getStackTraceString(e);
        } catch (BadPaddingException e) {
           // Log.getStackTraceString(e);
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] plaintext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(plaintext);
        } catch (NoSuchAlgorithmException e) {
            //Log.getStackTraceString(e);
        } catch (NoSuchPaddingException e) {
           // Log.getStackTraceString(e);
        } catch (InvalidKeyException e) {
           // Log.getStackTraceString(e);
        } catch (InvalidAlgorithmParameterException e) {
           // Log.getStackTraceString(e);
        } catch (IllegalBlockSizeException e) {
          //  Log.getStackTraceString(e);
        } catch (BadPaddingException e) {
           // Log.getStackTraceString(e);
        }
        return null;
    }
}
