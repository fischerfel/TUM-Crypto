import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESJavaScript {
    private SecretKeySpec key;
    private Cipher cipher;
    private int size = 128;
    private static final Charset CHARSET = Charset.forName("UTF-8"); 

    public AESJavaScript() throws NoSuchAlgorithmException,
            NoSuchPaddingException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(size); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        key = new SecretKeySpec(raw, "AES/CTR/NoPadding");
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
    }

    public void setKey(String keyText) {
        byte[] bText = new byte[size];
        bText = keyText.getBytes(CHARSET);
        key = new SecretKeySpec(bText, "AES/CTR/NoPadding");
    }

    public String encrypt(String message) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return byteArrayToHexString(encrypted);
    }

    public String decrypt(String hexCiphertext)
            throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(hexCiphertext));
        return byteArrayToHexString(decrypted);
    }

    private static String byteArrayToHexString(byte[] raw) {
        String hex = "0x";
        String s = new String(raw);
        for (int x = 0; x < s.length(); x++) {
            char[] t = s.substring(x, x + 1).toCharArray();
            hex += Integer.toHexString((int) t[0]).toUpperCase();
        }
        return hex;
    }

    private static byte[] hexStringToByteArray(String hex) {
        Pattern replace = Pattern.compile("^0x");
        String s = replace.matcher(hex).replaceAll("");

        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static void main(String[] args) {
        try {
            AESJavaScript ajs = new AESJavaScript();
            ajs.setKey("Secret Passphrase");
            String hexCiphertext = "U2FsdGVkX1/uYgVsNZmpbgKQJ8KD+8R8yyYn5+irhoI=";
            String decrypted = ajs.decrypt(hexCiphertext);
            System.out.println("decrypted > " + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
