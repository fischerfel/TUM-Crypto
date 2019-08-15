String PRIVATE_KEY = (String) "asf";
String dateInString = "2015-04-26";  // Start date
String sdf = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
String Token = (String) sdf + PRIVATE_KEY;

private static String toHexString(final byte[] bytes) {
    final Formatter formatter = new Formatter();
    for (final byte b : bytes) {
        formatter.format("%02x", b);
    }
    return formatter.toString();
}

public static String hmacSha256(final String PRIVATE_KEY, final String sdf) {
    try {
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(PRIVATE_KEY.getBytes(), "HmacSHA256"));
        return toHexString(mac.doFinal(sdf.getBytes()));
    }
    catch (final Exception e) {
        // ...
    }
    return PRIVATE_KEY;
}
