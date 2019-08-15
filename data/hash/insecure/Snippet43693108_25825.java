@Component
public class TokenUtils {

public static final String MAGIC_KEY = "obfuscate";

public String createToken(final UserDetails userDetails) {
    final long expires = System.currentTimeMillis() + 1000L * 60 * 60;
    return userDetails.getUsername() + ":" + expires + ":" + computeSignature(userDetails, expires);
}

public String computeSignature(UserDetails userDetails, long expires) {
    final StringBuilder signatureBuilder = new StringBuilder();
    signatureBuilder.append(userDetails.getUsername()).append(":");
    signatureBuilder.append(expires).append(":");
    signatureBuilder.append(userDetails.getPassword()).append(":");
    signatureBuilder.append(TokenUtils.MAGIC_KEY);

    MessageDigest digest;
    try {
        digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("No MD5 algorithm available!");
    }
    return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
}

public String getUserNameFromToken(final String authToken) {
    if (null == authToken) {
        return null;
    }
    final String[] parts = authToken.split(":");
    return parts[0];
}

public boolean validateToken(final String authToken, final UserDetails userDetails) {
    final String[] parts = authToken.split(":");
    final long expires = Long.parseLong(parts[1]);
    final String signature = parts[2];
    final String signatureToMatch = computeSignature(userDetails, expires);
    return expires >= System.currentTimeMillis() && signature.equals(signatureToMatch);
    }
}
