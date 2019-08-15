private byte[] hmacSHA256(String data, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(secretKey);
    mac.update(data.getBytes("UTF-8"));
    byte[] hmacData = mac.doFinal();
    return hmacData;
}
