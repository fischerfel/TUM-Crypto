import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AESToy3 {

    private static final Charset ASCII = Charset.forName("US-ASCII");

    public static void main(String[] args) throws Exception {
        String base64Cipher = "iz1qFlQJfs6Ycp+gcc2z4w==";
        byte [] cipherBytes = Base64.decodeBase64(base64Cipher);
        byte [] iv = "1234567812345678".getBytes(ASCII);
        byte [] keyBytes = "1234567812345678".getBytes(ASCII);

        SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

        byte[] result = cipher.doFinal(cipherBytes);
        System.out.println(Hex.encodeHexString(result));
    }

}
