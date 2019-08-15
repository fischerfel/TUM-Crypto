import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Usage:
 * <pre>
 * String crypto = SimpleCrypto.encrypt(masterpassword, cleartext)
 * ...
 * String cleartext = SimpleCrypto.decrypt(masterpassword, crypto)
 * </pre>
 * @author ferenc.hechler
 * @author Doguhan Uluca
 */
public class Encryptor {

    public static String encrypt(String seed, String cleartext) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] result = encrypt(rawKey, cleartext.getBytes());
            return toBase64(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] enc = fromBase64(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        SecretKey skey = new SecretKeySpec(seed, "AES");

        byte[] raw = skey.getEncoded();

        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toBase64(byte[] buf)
    {
        return Base64.encodeBytes(buf);
    }

    public static byte[] fromBase64(String str) throws Exception
    {
        return Base64.decode(str);
    }
}
