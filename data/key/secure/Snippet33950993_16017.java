public static String encodeXML(String key, String data) {
    String result = "";
    try {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        mac.init(secretKeySpec);
        result = Base64.encodeBase64String(mac.doFinal(data.getBytes("UTF-8")));
    } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
        log.error("exception occured when encording HmacSHA256 hash");
    }
    return result;
}
