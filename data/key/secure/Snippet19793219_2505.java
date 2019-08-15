import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACSigner {

    public static final String HMACSHA256 = "HmacSHA256";

    public String createSignature(final String messageToSign, final String clientSecret) {
        // How do I convert the client secret to the key byte array?
        SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(Charsets.UTF_8), HMACSHA256);

        try {
            Mac mac = Mac.getInstance(HMACSHA256);
            mac.init(secretKey);

            byte[] bytesToSign = messageToSign.getBytes(Charsets.US_ASCII);
            byte[] signature = mac.doFinal(bytesToSign);
            return Base64.encodeBase64URLSafeString(signature);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
