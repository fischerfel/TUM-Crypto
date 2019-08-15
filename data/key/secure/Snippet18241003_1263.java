private static String hashedBase64ValueOfDataWithSecretKey(String data, String secret) {
    try {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return Base64.encodeToString(rawHmac, 0);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
