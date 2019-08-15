import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// adapted from http://stackoverflow.com/a/1205272/
public class Crypt {
    private static byte[] ivBytes;
    private static Cipher cipher;
    private static SecretKeySpec keySpec;
    private static IvParameterSpec ivSpec;
    private static Random random;
    private static long seed;

    private Crypt() {
    }

    private static byte[] ivBytes() {
        random.setSeed(seed);
        byte[] bytes = new byte[7];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String encrypt(String key, String input) {
        prepare(key);
        byte[] inputBytes = input.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = new byte[cipher.getOutputSize(inputBytes.length)];
            int enc_len = cipher.update(inputBytes, 0, inputBytes.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            return new String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String key, String input) {
        prepare(key);
        byte[] inputBytes = input.getBytes();
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = new byte[cipher.getOutputSize(inputBytes.length)];
            int dec_len = cipher.update(inputBytes, 0, inputBytes.length, decrypted, 0);
            dec_len += cipher.doFinal(decrypted, dec_len);
            return new String(decrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static void prepare(String key) {
        byte[] keyBytes = new byte[7];
        byte[] bytesFromKey = key.getBytes();
        int length = keyBytes.length < bytesFromKey.length ? keyBytes.length : bytesFromKey.length;
        System.arraycopy(bytesFromKey, 0, keyBytes, 0, length);
        // if the length is smaller then keyBytes length, there are trailing
        // zeros in the keyBytes. Fill them with the use of the seed;
        if (length < keyBytes.length) {
            random.setSeed(seed);
            byte[] newBytes = new byte[keyBytes.length - length];
            random.nextBytes(newBytes);
            System.arraycopy(newBytes, 0, keyBytes, length, newBytes.length);
        }

        keySpec = new SecretKeySpec(keyBytes, "DES");
        ivSpec = new IvParameterSpec(ivBytes);
        try {
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public static void init(String id) {
        seed = id.hashCode();
        random = new Random(seed);
        ivBytes = ivBytes();
    }

    public static final void main(String[] args) {
        init("123456");
        String str = "ILikeTurtles";
        String key = "KEY123";
        String encrypted = encrypt(key, str);
        System.out.println(encrypted);
        System.out.println(decrypt(key, encrypted));
    }
}
