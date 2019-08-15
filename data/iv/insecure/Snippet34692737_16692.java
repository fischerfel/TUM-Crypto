import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security {
    private static byte[] IV_64 = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
    private static byte[] KEY_64 = new byte[] { 7, 1, 7, 7, 5, 5, 4, 7 };

    private static String KEY_TYPE = "DES";
    private static String ALGORITHM = "DES/CBC/PKCS5Padding";

    public static String GetString(byte[] value) {
        StringBuilder builder = new StringBuilder();
        for (byte i : value) {
            builder.append(String.format("%02X", i & 0xff));
        }
        return builder.toString();
    }

    public static byte[] GetByte(String value) {
        StringBuilder builder = new StringBuilder();
        for (char i : value.toCharArray()) {
            builder.append(String.format("%02X", i & 0xff));
        }
        return String.valueOf(builder).getBytes();
    }

    public static byte[] Encrypt(byte[] value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY_64, KEY_TYPE), new IvParameterSpec(IV_64));
            return cipher.doFinal(value);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] Decrypt(byte[] value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY_64, KEY_TYPE), new IvParameterSpec(IV_64));
            return cipher.doFinal(value);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
