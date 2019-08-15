import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class AESEncrypt {

    /**
     * Turns array of bytes into string
     * 
     * @param buf
     *            Array of bytes to convert to hex string
     * @return Generated hex string
     */
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) throws Exception {

        String message = "Test text!";

        // Get the KeyGenerator
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available

        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        System.out.println("Key: " + asHex(raw));

        // Instantiate the cipher

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal((args.length == 0 ? message : args[0]).getBytes());
        System.out.println("encrypted string: " + asHex(encrypted));

    }
}
