public static String encode(String key, String data) {
    try {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmac.init(secret_key);
        return new String(Hex.encodeHex(hmac.doFinal(data.getBytes("UTF-8"))));
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
