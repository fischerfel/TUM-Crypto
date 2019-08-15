private static String toHexString(final byte[] bytes) {
    final Formatter formatter = new Formatter();
    for (final byte b : bytes) {
        formatter.format("%02x", b);
    }
    return formatter.toString();
}

public static String hmacSha256(final String key, final String s) {
    try {
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256");
        return toHexString(mac.doFinal(s.getBytes()));
    }
    catch (final Exception e) {
        // ...
    }
}
