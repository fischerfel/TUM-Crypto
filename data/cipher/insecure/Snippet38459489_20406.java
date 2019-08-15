import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESTest {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("usage: java " + DESTest.class.getCanonicalName() + " -e|-d text key");
            return;
        }
        String mode = args[0].trim();
        String text = args[1].trim();
        String key = args[2].trim();
        try {
            String s = "-d".equalsIgnoreCase(mode) ? dec(text, key) : enc(text, key);
            System.out.println("\n" + ("-d".equalsIgnoreCase(mode) ? "decryted as [" : "encrypted as [") + s + "]");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String enc(String plainText, String key) throws UnsupportedEncodingException {
        return new String(encHex(des(plainText.getBytes("UTF-8"), key, Cipher.ENCRYPT_MODE)));
    }

    private static String dec(String encrypted, String key) throws UnsupportedEncodingException {
        return new String(des(decHex(encrypted), key, Cipher.DECRYPT_MODE), "UTF-8");
    }

    private static byte[] des(byte[] bytes, String key, int cipherMode) {
        final String encoding = "UTF-8";
        try {
            DESKeySpec desKey = new DESKeySpec(key.getBytes(encoding));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // SecretKey securekey = new SecretKeySpec(key.getBytes(encoding), "DES");//same result as the 3 lines above
            Cipher cipher = Cipher.getInstance("DES");
            SecureRandom random = new SecureRandom();
            cipher.init(cipherMode, securekey, random);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private static String encHex(byte[] bytes) {
        final char[] chars = new char[bytes.length * 2];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            chars[j++] = HEX_CHARS[(0xF0 & bytes[i]) >>> 4];
            chars[j++] = HEX_CHARS[0x0F & bytes[i]];
        }
        return new String(chars);
    }

    private static byte[] decHex(String hex) {
        final int len = hex.length();
        final byte[] bytes = new byte[len / 2];
        for (int i = 0, j = 0; j < len; i++) {
            int f = Character.digit(hex.charAt(j), 16) << 4;
            j++;
            f = f | Character.digit(hex.charAt(j), 16);
            j++;
            bytes[i] = (byte) (f & 0xFF);
        }
        return bytes;
    }
}
