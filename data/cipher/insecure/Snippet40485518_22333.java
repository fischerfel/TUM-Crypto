  package encypt.com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class Testing {

private static final String ALGORITHM = "AES";
private static final int ITERATIONS = 2;
private static final byte[] keyValue = 
    new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

public static String encrypt(String value, String salt) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);  
    c.init(Cipher.ENCRYPT_MODE, key);

    String valueToEnc = null;
    String eValue = value;
    for (int i = 0; i < ITERATIONS; i++) {
        valueToEnc = salt + eValue;
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        eValue = new BASE64Encoder().encode(encValue);
    }
    return eValue;
}

public static String decrypt(String value, String salt) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);

    String dValue = null;
    String valueToDecrypt = value;
    for (int i = 0; i < ITERATIONS; i++) {
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(valueToDecrypt);
        byte[] decValue = c.doFinal(decordedValue);
        dValue = new String(decValue).substring(salt.length());
        valueToDecrypt = dValue;
    }
    return dValue;
}

private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGORITHM);
    // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
    return key;
}
