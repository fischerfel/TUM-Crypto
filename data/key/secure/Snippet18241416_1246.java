private String getAppendedHeader(String str) {
    try {
        String hash = getHash(str);

        String signature = new String(Base64.encodeBase64(hash.getBytes()));
        StringBuilder sb = new StringBuilder();
        sb.append(PUBLIC_KEY).append(' ').append(signature);
        return sb.toString();
    } catch (NoSuchAlgorithmException _e) {
        LL.e("Get mac error: " + _e.getMessage());
        return null;
    } catch (InvalidKeyException _e) {
        LL.e("Init mac error: " + _e.getMessage());
        return null;
    }
}


private String getHash(String str) throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret = new SecretKeySpec(PRIVATE_KEY.getBytes(), "HmacSHA256");
    mac.init(secret);
    byte[] digest = mac.doFinal(str.getBytes());
    BigInteger hash = new BigInteger(1, digest);
    String hmac = hash.toString(16);
    if (hmac.length() % 2 != 0) {
        hmac = "0" + hmac;
    }
    return hmac;
}
