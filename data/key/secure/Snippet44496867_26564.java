private String hash_hmac(String str, String secret) {
        String ss = Base64.encodeToString(str.getBytes("UTF-8"));
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        String hash1 = Base64.encodeToString(sha256_HMAC.doFinal(ss.getBytes("UTF-8")));
        return hash1;
}
