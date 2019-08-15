public String HMAC_SHA256(String secret, String message)
{
    String hash="";
    try{
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
    }catch (Exception e)
    {

    }
    return hash.trim();
}
