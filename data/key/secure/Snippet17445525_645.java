public static void main(String[] args) throws SignatureException {
    String data = "GET"+"\n"+"webservices.amazon.com"+"\n"+"/onca/xml"+"\n"+"AWSAccessKeyId=AKIAIOSFODNN7EXAMPLE&ItemId=0679722769&Operation=ItemLookup&ResponeGroup=ItemAttributes%2COffers%2CImages%2CReviews&Service=AWSECommerceService&Timestamp=2009-01-01T12%3A00%3A00Z&Version=2009-01-06";
    String key = "1234567890";
    String result = calculateRFC2104HMAC(data, key);
    System.out.println(result);

}

private static final String HMAC_SHA_ALGORITHM = "HmacSHA256";


public static String calculateRFC2104HMAC(String data, String key)throws java.security.SignatureException{
    String result;
    try {

    // get an hmac_sha256 key from the raw key bytes
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA_ALGORITHM);

    // get an hmac_sha256 Mac instance and initialize with the signing key
    Mac mac = Mac.getInstance(HMAC_SHA_ALGORITHM);
    mac.init(signingKey);

    // compute the hmac256 on input data bytes
    byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));

    // base64-encode the hmac256
    result = Base64.encodeBase64String(rawHmac);

    } catch (Exception e) {
        throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
    }
    return result;
    }
