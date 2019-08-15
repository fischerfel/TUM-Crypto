private String generateAuthHeader(String verb, String resourceType, String resourceId, String date, String masterKeyBase64) throws Exception
{
    //Decode the master key, and setup the MAC object for signing.
    byte[] masterKeyBytes = Base64.decode(masterKeyBase64, Base64.NO_WRAP);
    Mac mac = Mac.getInstance("HMACSHA256");
    mac.init(new SecretKeySpec(masterKeyBytes, "HMACSHA256"));

    //Build the unsigned auth string.
    String stringToSign = verb + "\n"
            + resourceType + "\n"
            + resourceId + "\n"
            + date + "\n"
            + "\n";

    //Sign and encode the auth string.
    String signature = Base64.encodeToString(mac.doFinal(stringToSign.toLowerCase().getBytes("UTF8")), Base64.NO_WRAP);

    //Generate the auth header.
    String authHeader = URLEncoder.encode("type=master&ver=1.0&sig=" + signature, "UTF8");

    return authHeader;
}
