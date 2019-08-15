package so.example;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.*;

public class SO_AES192 {

private static final String _AES = "AES";
private static final String _AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
private static final String KEY_VALUE = "a-24byte-key-string-here";
private static final String SALT_VALUE = "16byte-salt-here";
private static final int ITERATIONS = 1;

private static IvParameterSpec ivParameterSpec;

public static String encryptHex(String value) throws Exception {
    Key key = generateKey();

    Cipher c = Cipher.getInstance(_AES_CBC_PKCS5Padding);
    ivParameterSpec = new IvParameterSpec(SALT_VALUE.getBytes());
    c.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

    String valueToEncrypt = null;
    String eValue = value;
    for (int i = 0; i < ITERATIONS; i++) {
//            valueToEncrypt = SALT_VALUE + eValue; // pre-pend salt - Length > sample length
        valueToEncrypt =  eValue;     // don't pre-pend salt  Length = sample length
        byte[] encValue = c.doFinal(valueToEncrypt.getBytes());
        eValue =  Hex.encodeHexString(encValue);
    }
    return eValue;
}

public static String decryptHex(String value) throws Exception {
    Key key = generateKey();

    Cipher c = Cipher.getInstance(_AES_CBC_PKCS5Padding);
    ivParameterSpec = new IvParameterSpec(SALT_VALUE.getBytes());
    c.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

    String dValue = null;
    char[] valueToDecrypt = value.toCharArray();
    for (int i = 0; i < ITERATIONS; i++) {
        byte[] decordedValue = Hex.decodeHex(valueToDecrypt);
        byte[] decValue = c.doFinal(decordedValue);
//            dValue = new String(decValue).substring(SALT_VALUE.length()); // when salt is pre-pended
        dValue = new String(decValue);   // when salt is not pre-pended
        valueToDecrypt = dValue.toCharArray();
    }
    return dValue;
}

private static Key generateKey() throws Exception {
    // Key key = new SecretKeySpec(KEY_VALUE.getBytes(), _AES); // this was wrong
    Key key = new SecretKeySpec(new BASE64Decoder().decodeBuffer(keyValueString), _AES); // had to un-Base64 the 'known' 24-byte key.
    return key;
}

}
