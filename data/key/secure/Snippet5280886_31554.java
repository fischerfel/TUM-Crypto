public static String generateHmacSHA256Signature(String data, String key)   throws GeneralSecurityException {
    byte[] hmacData = null;

    try {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new BASE64Encoder().encode(hmacData);
    } catch (UnsupportedEncodingException e) {
        // TODO: handle exception
        throw new GeneralSecurityException(e);
    }
}
