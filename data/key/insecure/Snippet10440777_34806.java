import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
    private static byte[] KEY = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2' };

    public static byte[] decrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CFB/NoPadding");
        Key key = new SecretKeySpec(KEY, "AES");
        c.init(Cipher.DECRYPT_MODE, key);
        return c.doFinal(data);
    }
}
