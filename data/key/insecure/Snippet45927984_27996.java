private static final String HMAC_SHA1_ALGORITHM = "HmacSHA384";

private static String bytesToHex(final byte[] hash) {
    final StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
        final String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) {
            hexString.append('0');
        }
        hexString.append(hex);
    }
    return hexString.toString();
}

public static void main(final String... args) throws NoSuchAlgorithmException, InvalidKeyException {
    final String nonce = "nonce";
    final String message = "test string";
    SecretKeySpec signingKey = new SecretKeySpec(nonce.getBytes(), HMAC_SHA1_ALGORITHM);
    final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
    mac.init(signingKey);
    System.out.println(bytesToHex(mac.doFinal(message.getBytes())));
}
