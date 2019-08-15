private String hash_hmac(String str, String secret) throws Exception{
Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
sha256_HMAC.init(secretKey);
String hash = Base64.encodeToString(sha256_HMAC.doFinal(str.getBytes()), Base64.DEFAULT);
return hash;
}
