import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SSL {

    private final static String HEX = "0123456789ABCDEF";
    private final static String ENC = "US-ASCII";
    private final static int ITERATION = 1337;

    private static final String RANDOM_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";

    private static IvParameterSpec ips;

    public static void init(byte[] iv) {
        if(iv == null) {
            iv = new byte[16];

            Random random = new Random();
            random.nextBytes(iv);
        }

        ips = new IvParameterSpec(iv);

        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] getCertificate() {
        return ips.getIV();
    }

    public static String encrypt(Session current, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(current.getCurrentSession().toCharArray());
        byte[] result = encrypt(rawKey, cleartext.getBytes(ENC));
        return toHex(result);
    }

    public static String decrypt(Session current, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(current.getCurrentSession().toCharArray());
        byte[] enc = toByte(encrypted);     
        byte[] result = decrypt(rawKey, enc);
        return new String(result, ENC);
    }

    private static byte[] getRawKey(char[] seed) throws Exception {
        KeySpec keySpec = new PBEKeySpec(seed, ips.getIV(), ITERATION);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(RANDOM_ALGORITHM);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        return secretKey.getEncoded();
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, SECRET_KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, SECRET_KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) throws UnsupportedEncodingException {
        return toHex(txt.getBytes(ENC));
    }
    public static String fromHex(String hex) throws UnsupportedEncodingException {
        return new String(toByte(hex), ENC);
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
}
