import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Test {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "ADBSJHJS12547896".getBytes();

    public static void main(String args[]) throws Exception {
        String encriptValue = encrypt("dude5");
        decrypt(encriptValue);

    }

    /**
     * @param args
     * @throws Exception
     */

    public static String encrypt(String valueToEnc) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        System.out.println("valueToEnc.getBytes().length "+valueToEnc.getBytes().length);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        System.out.println("encValue length" + encValue.length);
        byte[] encryptedByteValue = new Base64().encode(encValue);
        String encryptedValue = encryptedByteValue.toString();
        System.out.println("encryptedValue " + encryptedValue);

        return encryptedValue;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        byte[] enctVal = c.doFinal(encryptedValue.getBytes());
        System.out.println("enctVal length " + enctVal.length);

        byte[] decordedValue = new Base64().decode(enctVal);

        return decordedValue.toString();
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }

}
