import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.tdocc.utils.Base64;

public class TripleDES {
    private static byte[] keyBytes = { 110, 32, 73, 24, 125, 66, 75, 18, 79, (byte)150, (byte)211, 122, (byte)213, 14, (byte)156, (byte)136, (byte)171, (byte)218, 119, (byte)240, 81, (byte)142, 23, 4 };
    private static byte[] ivBytes = { 25, 117, 68, 23, 99, 78, (byte)231, (byte)219 };

    public static String encryptText(String plainText) {
        try {
            if (plainText.isEmpty()) return plainText;
            return Base64.decode(TripleDES.encrypt(plainText)).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] encrypt(String plainText) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        try {
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] plainTextBytes = plainText.getBytes("utf-8");
            final byte[] cipherText = cipher.doFinal(plainTextBytes);

            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decryptText(String message) {
        try {
            if (message.isEmpty()) return message;
            else return TripleDES.decrypt(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(byte[] message) {
        try {
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(ivBytes);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            final byte[] plainText = cipher.doFinal(message);

            return plainText.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
