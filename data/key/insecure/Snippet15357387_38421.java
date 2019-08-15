import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class MCrypt {

    private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String SecretKey = "0123456789abcdef";//Dummy secretKey (CHANGE IT!)

    public MCrypt() {
        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) throw new Exception("Empty string");

        byte[] encrypted = null;

        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

        encrypted = cipher.doFinal(padString(text).getBytes());
        try { //  for OS version 2.2+
            encrypted = android.util.Base64.encode(encrypted, android.util.Base64.NO_PADDING);
        } catch (NoClassDefFoundError e) {
            encrypted = org.apache.commons.codec.binary.Base64.encodeBase64(encrypted);
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) throw new Exception("Empty string");

        byte[] decrypted = null;

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        try { // for OS version 2.2+
            decrypted = cipher.doFinal(android.util.Base64.decode(code, android.util.Base64.NO_PADDING));
        } catch (NoClassDefFoundError e) {
            decrypted = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(code.getBytes("UTF-8")));
        }
        return decrypted;
    }



    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }
}
