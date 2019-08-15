public String hashMacSha256(String message, String key) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),"HmacSHA256");
    sha256_HMAC.init(secret_key);
    String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
    return hash;
}
