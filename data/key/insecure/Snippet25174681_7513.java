private static final String HMAC_SHA1 = "HmacSHA1";

private static final String ENC = "UTF-8";

private String getSignature(String url, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {

    StringBuilder base = new StringBuilder();
    base.append("GET&");
    base.append(url);
    base.append("&");
    base.append(params);
    System.out.println("String for oauth_signature generation:" + base);

    byte[] keyBytes = (DIGITAL_CONSUMER_SECRET + "&").getBytes();

    SecretKey key = new SecretKeySpec(keyBytes, HMAC_SHA1);

    Mac mac = Mac.getInstance(HMAC_SHA1);
    mac.init(key);

    return new String(base64.encode(mac.doFinal(base.toString().getBytes(
            ENC))), ENC).trim();
}
