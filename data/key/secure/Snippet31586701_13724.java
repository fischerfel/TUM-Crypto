public static String calculateHMAC(String data, String key) throws Exception {
String result;
try {
    // get an hmac_sha2 key from the raw key bytes
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA2_ALGORITHM);

    // get an hmac_sha1 Mac instance and initialize with the signing key
    Mac sha256_HMAC = Mac.getInstance(HMAC_SHA2_ALGORITHM);
    sha256_HMAC.init(signingKey);

    // compute the hmac on input data bytes
    byte[] rawHmac = sha256_HMAC.doFinal(data.getBytes());

    // base64-encode the hmac 
    StringBuilder sb = new StringBuilder();
    char[] charArray = Base64.encode(rawHmac);
        for ( char a : charArray){
            sb.append(a);
            }
        result = sb.toString();
    }
    catch (Exception e) {
        throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
    }
    return result;
}
