public static String encryptDataRSA(final String data) throws IOException {  
  final byte[] dataToEncrypt = data.getBytes();
  byte[] encryptedData = null;  

  try {
    final String keyStr = "-----BEGIN PUBLIC KEY-----\n" +
                            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdQudusozLmogBfU2LCO+WcM59\n" +
                            "ycup9SxMsBNCku23PxrPMO6u//QjtWPz7istE9vkQfa6tQn1Or+SDxeHLMxEesF0\n" +
                            "xiBEgFUhg7vjOF2SnFQQEADgUyizUIBBn1UgKNA8eP24Ux0P0M2aHMn78HIHsRcu\n" +
                            "pNGUNW7p51HOVoIPJQIDAQAB\n" +
                            "-----END PUBLIC KEY-----";

    PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyStr.getBytes()));

    final Cipher cipher = Cipher.getInstance("RSA");  
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
    encryptedData = cipher.doFinal(dataToEncrypt); 

    try {
      final String encryptedText = new String(Base64.encode(encryptedData, Base64.DEFAULT), "UTF-8");
      return encryptedText.toString();
    } 
    catch (final UnsupportedEncodingException e1) { return null; }      
  } catch (Exception e) { e.printStackTrace(); }     

  return "ERROR";
}
