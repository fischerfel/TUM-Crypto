import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


    //openssl enc -nosalt -aes-128-ecb
    // -in <input file>
    // -out <output file>
    // -K <16 bytes in hex, for example : "abc" can be hashed in SHA-1, the first 16 bytes in hex is a9993e364706816aba3e25717850c26c>
    private final static String TRANSFORMATION = "AES"; // use aes-128-ecb in openssl

public static byte[] encrypt(String passcode, byte[] data) throws CryptographicException {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, genKeySpec(passcode));
            return cipher.doFinal(data);
        } catch (Exception ex) {
            throw new CryptographicException("Error encrypting", ex);
        }
    }


    public static String encryptWithBase64(String passcode, byte[] data) throws CryptographicException {
        return new BASE64Encoder().encode(encrypt(passcode, data));
    }

    public static byte[] decrypt(String passcode, byte[] data) throws CryptographicException {
        try {
            Cipher dcipher = Cipher.getInstance(TRANSFORMATION);
            dcipher.init(Cipher.DECRYPT_MODE, genKeySpec(passcode));
            return dcipher.doFinal(data);
        } catch (Exception e) {
            throw new CryptographicException("Error decrypting", e);
        }
    }


    public static byte[] decryptWithBase64(String passcode, String encrptedStr) throws CryptographicException {
        try {
            return decrypt(passcode, new BASE64Decoder().decodeBuffer(encrptedStr));
        } catch (Exception e) {
            throw new CryptographicException("Error decrypting", e);
        }
    }

    public static SecretKeySpec genKeySpec(String passcode) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = passcode.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        return new SecretKeySpec(key, TRANSFORMATION);
    }
