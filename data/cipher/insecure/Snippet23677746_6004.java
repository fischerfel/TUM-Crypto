import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncTest {

public static String decryptString(String str, String key) throws Exception {

    str = URLDecoder.decode(str, "UTF-8");
    String result = java.net.URLDecoder.decode(str, "UTF-8");
    byte[] keyBytes = key.getBytes("UTF-8");

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    BASE64Decoder base64decoder = new BASE64Decoder();

    byte[] clrtxt = base64decoder.decodeBuffer(result);
    byte[] cphtxt = cipher.doFinal(clrtxt);

    StringBuffer sBuffer = new StringBuffer();

    for (int i = 0; i < cphtxt.length; i++) {
        sBuffer.append((char) cphtxt[i]);
    }

    return sBuffer.toString();

}

public static String encryptString(String str, String key) throws Exception {

    byte[] keyBytes = key.getBytes("UTF-8");
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DESede");
    Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);

    byte[] clrtxt = str.getBytes("UTF8");
    byte[] cphtxt = cipher.doFinal(clrtxt);

    BASE64Encoder base64encoder = new BASE64Encoder();

    return URLEncoder.encode(base64encoder.encode(cphtxt), "UTF-8");
}
