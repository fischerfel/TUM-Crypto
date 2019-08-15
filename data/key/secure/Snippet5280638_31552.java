import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64Encoder;

public class GenSign2 {
    public static void main(String[] args) throws GeneralSecurityException,
            IOException {
        String secretKey = "M2Y2YjkxZDEtYmNlOC1mYmI0LTkxZTgtOTNiY2RiMDhmN2E2YjExNGUwYjktNGJkYy1jZTM0LWQ1MWYtZGIwYWRlZTE0NGNh";
        String salt = "0123456789";

        String generateHmacSHA256Signature = generateHmacSHA256Signature(salt,
                secretKey);
        System.out.println("Signature: " + generateHmacSHA256Signature);

        String urlEncodedSign = URLEncoder.encode(generateHmacSHA256Signature,
                "UTF-8");

        System.out.println("Url encoded value: " + urlEncodedSign);
    }

    public static String generateHmacSHA256Signature(String data, String key)
            throws GeneralSecurityException, IOException {
        byte[] hmacData = null;

        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),
                    "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            hmacData = mac.doFinal(data.getBytes("UTF-8"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            new Base64Encoder().encode(hmacData, 0, hmacData.length, bout);
            return bout.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
    }
}
