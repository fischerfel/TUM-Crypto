import java.nio.charset.Charset;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.*;

public class AES {

    public static String decrypt(String encryptedData) throws Exception {
        byte[] keyBytes = "SecretPassphrase".getBytes();
        Key key = new SecretKeySpec(keyBytes, "AES");

        Cipher c = Cipher.getInstance(ALGO);

        byte[] iv = (byte[]) new Hex().decode("a43e384b24e275c29a8a68bc031fd79e");
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.DECRYPT_MODE, key, ivspec);

        byte[] decordedValue = (byte[]) new Hex().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);

        String decryptedValue = Hex.encodeHexString(decValue);
        return decryptedValue;
    }

    public static void main(String[] args) throws Exception {
        String result = AES.decrypt("c86b6ca4ef30fadfea28821e04aa8dad");
        System.out.println(hexToString(result));
    }
}
