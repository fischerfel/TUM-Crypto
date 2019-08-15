package tokens;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TokenUtils {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String TOKEN_SEPARATOR = ":";
    private static final long MAX_AGE = 1_000 * 60 * 60 * 24; // 24h

    private TokenUtils() {
    }

    public static String createToken(String username, long timestamp, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(generateTokenStringPublicPart(username, timestamp));
        sb.append(TOKEN_SEPARATOR);
        sb.append(computeSignature(username, timestamp, secretKey));
        return sb.toString();
    }

    public static boolean verifyToken(String token, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        String[] parts = token.split(TOKEN_SEPARATOR);
        boolean result = false;
        if (parts.length == 3) {
            String username = parts[0];
            Long timestamp = Long.valueOf(parts[1]);
            String signature = parts[2];
            if (signature.equals(computeSignature(username, timestamp, secretKey))) {
                if (System.currentTimeMillis() - timestamp < MAX_AGE) {
                    result = true;
                }
            }
        }
        return result;
    }

    private static String generateTokenStringPublicPart(String username, long timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        sb.append(TOKEN_SEPARATOR);
        sb.append(timestamp);
        return sb.toString();
    }

    private static String computeSignature(String username, long timestamp, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(generateTokenStringPublicPart(username, timestamp));
        SecretKeySpec sks = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        Mac hmac = Mac.getInstance(HMAC_ALGO);
        hmac.init(sks);
        return Base64.getUrlEncoder().encodeToString(hmac.doFinal(sb.toString().getBytes(StandardCharsets.UTF_8)));
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        String secretKey = "secret_key";
        String token = TokenUtils.createToken("marko", System.currentTimeMillis(), secretKey);
        System.out.println(token);
        System.out.println("Original token verification: " + TokenUtils.verifyToken(token, secretKey));
        token = token.replaceAll("a", "b");
        System.out.println("Tampered token verification: " + TokenUtils.verifyToken(token, secretKey));
        token = TokenUtils.createToken("marko", System.currentTimeMillis() - 1_000 * 60 * 60 * 48, secretKey);
        System.out.println("Expired token verification: " + TokenUtils.verifyToken(token, secretKey));
    }

}
