public String generateHMAC(final String messge,final String key) throws APIGatewayException {

    String result;
    try {

        // get an hmac_sha1 key from the raw key bytes
        final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

        // get an hmac_sha1 Mac instance and initialize with the signing key
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        // compute the hmac on input data bytes
        final byte[] rawHmac = mac.doFinal(messge.getBytes());

        // base64-encode the hmac
        result = DatatypeConverter.printBase64Binary(rawHmac);

    } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
        throw new APIGatewayException("Failed to generate HMAC : " + e.getMessage());
    }
    return result;

}
