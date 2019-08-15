import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleCrypto {

    private static final int KEY_SIZE = 128;

    public static String encrypt(String seed, String cleartext) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        final byte[] rawKey = getRawKey(seed.getBytes());
        final byte[] result = encrypt(rawKey, cleartext.getBytes());
        return bin2hex(result);
    }

    public static String decrypt(String seed, String encrypted) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        final byte[] rawKey = getRawKey(seed.getBytes());
        final byte[] enc = toByte(encrypted);
        final byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static String decrypt(String seed, byte[] encrypted) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        final byte[] rawKey = getRawKey(seed.getBytes());
        final byte[] result = decrypt(rawKey, encrypted);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException {
        final KeyGenerator kgen = KeyGenerator.getInstance("AES");
        final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(KEY_SIZE, sr); // 192 and 256 bits may not be available
        final SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        final byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        final byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return bin2hex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        final int len = hexString.length() / 2;
        final byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static byte[] getHash(String str) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        digest.reset();
        return digest.digest(str.getBytes());
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }
}
