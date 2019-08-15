private String generateSignature(String baseString, String secret) throws          UnsupportedEncodingException {
    String secretKey = consumerSecret + "&";


    SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1SignatureMethod.SIGNATURE_NAME);
    HMAC_SHA1SignatureMethod hmacsha = new HMAC_SHA1SignatureMethod(keySpec);
    String signatureString = hmacsha.sign(baseString);
    String base64Encode = new String((signatureString.getBytes()));
    signature = URLEncoder.encode(base64Encode, "UTF-8");
