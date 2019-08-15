/**
 *
 * @author MUDASSIR
 */

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class AESencrp2 {

     private static final String ALGO = "AES";
//    private static final byte[] keyValue = 
//        new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',
//'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

     public static String asHex(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

public static String encrypt(String Data, byte[] keyValue) throws Exception {
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = c.doFinal(Data.getBytes());
        return asHex(encryptedValue);
    }

    public static String decrypt(String encryptedData, byte[] keyValue) throws Exception {
        Key key = generateKey(keyValue);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = c.doFinal(encryptedData.getBytes());
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static Key generateKey(byte[] keyValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
}

}
