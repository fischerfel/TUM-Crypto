import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Encryptor {

    @Value("${aeskey}")
    private String keyStr;

    private Key aesKey = null;
    private Cipher cipher = null;

    synchronized private void init() throws Exception {
        if (keyStr == null || keyStr.length() != 16) {
            throw new Exception("bad aes key configured");
        }
        if (aesKey == null) {
            aesKey = new SecretKeySpec(keyStr.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        }
    }

    synchronized public String encrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return toHexString(cipher.doFinal(text.getBytes()));
    }

    synchronized public String decrypt(String text) throws Exception {
        init();
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(toByteArray(text)));
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    /*
     * DO NOT DELETE
     * 
     * Use this commented code if you don't like using DatatypeConverter dependency
     */
    // public static String toHexStringOld(byte[] bytes) {
    // StringBuilder sb = new StringBuilder();
    // for (byte b : bytes) {
    // sb.append(String.format("%02X", b));
    // }
    // return sb.toString();
    // }
    //
    // public static byte[] toByteArrayOld(String s) {
    // int len = s.length();
    // byte[] data = new byte[len / 2];
    // for (int i = 0; i < len; i += 2) {
    // data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i +
    // 1), 16));
    // }
    // return data;
    // }
}
