import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WLChecker {

    private final String clientSecret;

    public WLChecker(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @throws GeneralSecurityException if the token is not perfect
     */
    public String check(String tokenString) throws IOException, GeneralSecurityException {
        final String[] parts = tokenString.split("\\.");
        if (parts.length != 3)
            throw new GeneralSecurityException("Not a valid token");

        validate(parts[0], parts[1], parts[2]);

        JsonNode claims = new ObjectMapper().readTree(BaseEncoding.base64Url().decode(parts[1]));

        String uid = claims.path("uid").asText();
        if (uid == null || uid.length() == 0)
            throw new GeneralSecurityException("No uid in claims");

        return uid;
    }

    private void validate(String envelope, String claims, String sig) throws GeneralSecurityException {
        byte[] signingKey = sha256(getBytesUTF8(clientSecret + "JWTSig"));
        byte[] input = getBytesUTF8(envelope + "." + claims);

        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(signingKey, "HmacSHA256"));

        byte[] calculated = hmac.doFinal(input);

        if (!Arrays.equals(calculated, BaseEncoding.base64Url().decode(sig)))
            throw new GeneralSecurityException("Signature verification failed");
    }

    private byte[] getBytesUTF8(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
