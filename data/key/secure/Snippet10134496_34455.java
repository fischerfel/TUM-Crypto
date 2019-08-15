public static boolean validateUserSignature(String UID, String timestamp, String secret, String signature) throws InvalidKeyException, UnsupportedEncodingException
{
    String expectedSig = calcSignature("HmacSHA1", timestamp+"_"+UID, Base64.decode(secret)); 
    return expectedSig.equals(signature);   
}

private static String calcSignature(String algorithmName, String text, byte[] key) throws InvalidKeyException, UnsupportedEncodingException  
{
    byte[] textData  = text.getBytes("UTF-8");
    SecretKeySpec signingKey = new SecretKeySpec(key, algorithmName);

    Mac mac;
    try {
        mac = Mac.getInstance(algorithmName);
    } catch (NoSuchAlgorithmException e) {
        return null;
    }

    mac.init(signingKey);
    byte[] rawHmac = mac.doFinal(textData);

    return Base64.encodeToString(rawHmac, false);           
}
