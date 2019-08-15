private String getSecretHash(String email, String appClientId, String appSecretKey) throws Exception {
    byte[] data = (email + appClientId).getBytes("UTF-8");
    byte[] key = appSecretKey.getBytes("UTF-8");

    return Base64.encodeAsString(HmacSHA256(data, key));
}

static byte[] HmacSHA256(byte[] data, byte[] key) throws Exception {
    String algorithm = "HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data);
}
