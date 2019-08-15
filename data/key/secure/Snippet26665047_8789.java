public static String generateAuthHeader(String verb, String resourceId,
        String resourceType, String date, String masterKeyBase64)
        throws Exception {

    // Decode the master key and setup the MAC object for signing.
    byte[] masterKeyBytes = Base64.decodeBase64(masterKeyBase64.getBytes());
    Mac mac = Mac.getInstance("HMACSHA256");
    mac.init(new SecretKeySpec(masterKeyBytes, "HMACSHA256"));

    // Build the unsigned authorization string.
    String stringToSign = verb + "\n"
            + resourceType + "\n"
            + resourceId + "\n"
            + date + "\n"
            + "\n";

    // Sign and encode the authorization string.
    String signature = Base64.encodeBase64String(mac.doFinal(stringToSign.toLowerCase().getBytes()));

    // Generate the authorization header.
    String authHeader = URLEncoder.encode("type=master&ver=1.0&sig=" + signature, "UTF-8");

    return authHeader;
}
