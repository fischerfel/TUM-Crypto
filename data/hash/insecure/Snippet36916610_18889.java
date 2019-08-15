public class ZookeeperSecurityUtil {

private static final String SHA1 = "SHA1";
private static final String COLON = ":";
private static final String DIGEST_SCHEME = "digest";

public static List<AuthInfo> getCredentialsFromSystemProperties() {
    final List<AuthInfo> authInfo = new ArrayList<>();

    final String user = System.getProperty("zookeeper.security.user");
    final String password = System.getProperty("zookeeper.security.password");

    authInfo.add(new AuthInfo(DIGEST_SCHEME, new String(user + COLON + password).getBytes(Charsets.UTF_8)));

    return authInfo;
}
public static String generateDigest(final String idPassword) throws NoSuchAlgorithmException {
    final String parts[] = idPassword.split(COLON, 2);
    final byte digest[] = MessageDigest.getInstance(SHA1).digest(idPassword.getBytes());
    return parts[0] + COLON + base64Encode(digest);
}
private static String base64Encode(final byte byteDigest[]) {

    return new String(Base64.getEncoder().encode(byteDigest));
}
}
