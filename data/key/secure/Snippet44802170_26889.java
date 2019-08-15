private String getSignature(Map params) throws NoSuchAlgorithmException, InvalidKeyException {

    String plainText = "GET" + "\n" + params.endpointUrl + "\n" + "" + "\n" + timess
    Mac mac = Mac.getInstance("HmacSHA256")
    SecretKeySpec secretKeySpec = new SecretKeySpec(params.password.getBytes(), "HmacSHA256")
    mac.init(secretKeySpec)
    byte[] rawHmac = mac.doFinal(plainText.getBytes())
    return DatatypeConverter.printHexBinary(rawHmac)
}
