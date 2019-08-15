public static VerificationResult verifySubscriptionPostRequestSignature(String clientSecret, byte[] rawJsonData, String xHubSignature) throws InstagramException{
    SecretKeySpec keySpec;
    keySpec = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), HMAC_SHA1);
    Mac mac;

    try {
        mac = Mac.getInstance(HMAC_SHA1);
        mac.init(keySpec);
        byte[] result = mac.doFinal(rawJsonData);
        String encodedResult = Hex.encodeHexString(result);

        return new VerificationResult(encodedResult.equals(xHubSignature), encodedResult);
    } catch (NoSuchAlgorithmException e) {
        throw new InstagramException("Invalid algorithm name!", e);
    } catch (InvalidKeyException e){
        throw new InstagramException("Invalid key: " + clientSecret, e);
    }
}
