public static VerificationResult verifySubscriptionPostSignature(String clientSecret, String rawJsonData, String xHubSignature) {
    SecretKeySpec keySpec;
    keySpec = new SecretKeySpec(clientSecret.getBytes("UTF-8"), HMAC_SHA1);

    Mac mac;
    mac = Mac.getInstance(HMAC_SHA1);
    mac.init(keySpec);

    byte[] result;
    result = mac.doFinal(rawJsonData.getBytes("UTF-8"));
    String encodedResult = Hex.encodeHexString(result);

    return new VerificationResult(encodedResult.equals(xHubSignature), encodedResult);
   }
