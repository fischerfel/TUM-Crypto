import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class Main {
    private final static int GCM_IV_LENGTH = 12;
    private final static int GCM_TAG_LENGTH = 16;

    private static String encrypt(String privateString, SecretKey skey) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        (new SecureRandom()).nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);

        byte[] ciphertext = cipher.doFinal(privateString.getBytes("UTF8"));
        byte[] encrypted = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, encrypted, 0, iv.length);
        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);

        String encoded = Base64.getEncoder().encodeToString(encrypted);

        return encoded;
    }

    private static String decrypt(String encrypted, SecretKey skey) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encrypted);

        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

        byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

        String result = new String(ciphertext, "UTF8");

        return result;
    }

    public static void main(String[] args) throws Exception {
        SecretKey key = new SecretKeySpec(new byte[16], "AES"); // key is 16 zero bytes
        String s = decrypt(encrypt("This is the first string to test", key), key);
        System.out.println(s);
    }
}
