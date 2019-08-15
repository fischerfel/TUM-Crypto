        package encript;

import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESencrp {

    KeyGenerator gen;
    private static final byte[] keyValue = new byte[16] ;

    public AESencrp() throws NoSuchAlgorithmException {
        this.gen = KeyGenerator.getInstance("AES");
        gen.init(128); /* 128-bit AES */

        SecretKey secret = gen.generateKey();
        byte[] keyValue = secret.getEncoded();
        String text = String.format("%032X", new BigInteger(+1, keyValue));
        System.out.println(text);
    }

    private static final String ALGO = "AES";
    /*private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B',
            'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };*/

    private static String toHex(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (final byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}
