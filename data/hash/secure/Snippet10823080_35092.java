public final class OneWayEncryptor {
private static final OneWayEncryptor INSTANCE = new OneWayEncryptor();

private static final int ITERATIONS = 1024;
private static final String ALGORITHM = "SHA-256";

private OneWayEncryptor() {
}

public static OneWayEncryptor getInstance() {
    return INSTANCE;

}

public String encrypt(String plaintext, String salt) throws NoSuchAlgorithmException,
        UnsupportedEncodingException {
    MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);

    messageDigest.reset();
    messageDigest.update(salt.getBytes());

    byte[] btPass = messageDigest.digest(plaintext.getBytes("UTF-8"));
    for (int i = 0; i < ITERATIONS; i++) {
        messageDigest.reset();
        btPass = messageDigest.digest(btPass);
    }

    String encodedPassword = byteToBase64(btPass);

    return encodedPassword;
}

private String byteToBase64(byte[] bt) throws UnsupportedEncodingException {

    return new String(Base64.encodeBase64(bt));

}
