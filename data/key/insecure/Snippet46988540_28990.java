public static String HMAC_SHA(){
    try {
        String secret = "0393e944ee8108bb66fc9fa4f99f9c862481e9e0519e18232ba61b0767eee8c6";
        String message = "example";
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        String hash = android.util.Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.URL_SAFE);
        return new String(Hex.encodeHex(hash.getBytes()));
    }
    catch (Exception e){
        e.printStackTrace();
    }
    return null;
}
