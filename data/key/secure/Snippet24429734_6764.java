public static String base64sha256(String data, String secret) {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] res = sha256_HMAC.doFinal(data.getBytes());
    return Base64.encodeToString(res, Base64.NO_WRAP);
}
