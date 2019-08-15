import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityKey {
    private static Key key = null;
    private static String encode = "UTF-8";
    private static String cipherKey = "DES/ECB/PKCS5Padding";

    static  {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            String seedStr = "test";
            generator.init(new SecureRandom(seedStr.getBytes()));
            key = generator.generateKey();
        } catch(Exception e) {
        }
    }

    // SecurityKey.decodeKey("password")
    public static String decodeKey(String str) throws Exception  {
        if(str == null)
            return str;

        Cipher cipher = null;
        byte[] raw = null;
        BASE64Decoder decoder = new BASE64Decoder();
        String result = null;
        cipher = Cipher.getInstance(cipherKey);
        cipher.init(Cipher.DECRYPT_MODE, key);
        raw = decoder.decodeBuffer(str);
        byte[] stringBytes = null;
        stringBytes = cipher.doFinal(raw); // Exception!!!!
        result = new String(stringBytes, encode);

        return result;
    }
}
