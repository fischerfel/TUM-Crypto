public static String getSignature(String secret, String message) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);

    String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
    hash = URLEncoder.encode(hash, "UTF-8");

    return hash;
}
