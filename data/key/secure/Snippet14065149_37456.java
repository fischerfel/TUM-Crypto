public static void main(String[] args) throws GeneralSecurityException, IOException {

    String secretKey = "secretKey";
    String salt = "0123456789";

    String generateHmacSHA256Signature = generateHmacSHA256Signature(salt, secretKey);
    System.out.println("Signature: " + generateHmacSHA256Signature);

    String urlEncodedSign = URLEncoder.encode(generateHmacSHA256Signature, "UTF-8");

    System.out.println("Url encoded value: " + urlEncodedSign);
}

public static String generateHmacSHA256Signature(String data, String key) throws GeneralSecurityException {
    byte[] hmacData = null;

    try {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new Base64Encoder().encode(hmacData);
    } catch (UnsupportedEncodingException e) {
        throw new GeneralSecurityException(e);
    }
}
