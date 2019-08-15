package org.kamal.crypto;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class SimpleProtector
{
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = 
        new byte[] { 'P', 'u', 't', ' ', 'Y', 'o', 'u', 'r', ' ', 'S', 'e', 'c', 'r', 'e', 't', ' ', 'K', 'e', 'y', '', 'H', 'e', 'r', 'e'};

    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        // key = keyFactory.generateSecret(new DESKeySpec(keyValue));
        return key;
    }
}
