String unencryptedMessage = "[1312701386,transactioncreate,[account_code:ABC,amount_in_cents:5000,currency:USD]]";
String privateKey = "0123456789ABCDEF0123456789ABCDEF";
String encryptedMessage = getHMACSHA1(unencryptedMessage, getSHA1(privateKey));

private static byte[] getSHA1(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] bytes = md.digest(source.getBytes("UTF-8"));
    return bytes;
}

private static String getHMACSHA1(String baseString, byte[] keyBytes) throws GeneralSecurityException, UnsupportedEncodingException {
    SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(secretKey);
    byte[] bytes = baseString.getBytes("ASCII");
    return Hex.encodeHexString(mac.doFinal(bytes));
}
