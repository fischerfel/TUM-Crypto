public static void main(String[] args) throws Exception {  
String pubKey_from_go="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZsfv1qscqYdy4vY+P4e3cAtmv"+
            "ppXQcRvrF1cB4drkv0haU24Y7m5qYtT52Kr539RdbKKdLAM6s20lWy7+5C0Dgacd"+
            "wYWd/7PeCELyEipZJL07Vro7Ate8Bfjya+wltGK9+XNUIHiumUKULW4KDx21+1NL"+
            "AUeJ6PeW+DAkmJWF6QIDAQAB";

String plainText = "Hello,I am  plaintext string!@sina.com"; 
String encryptString=encByGoPubKey(pubKey_from_go,plainText);
}
public static String encByGoPubKey(String pubkey_from_go,String plainText) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");//Cipher.getInstance("RSA/ECB/PKCS1Padding");  
    byte[] plainTextBytes = plainText.getBytes();  

    PublicKey pubkey_go=getPublicKey(pubkey_from_go);
    cipher.init(Cipher.ENCRYPT_MODE, pubkey_go);  
    byte[] enBytes = cipher.doFinal(plainTextBytes);  
    String encryptString = (new BASE64Encoder()).encode(enBytes);  
    return encryptString;   
}
public static PublicKey getPublicKey(String key) throws Exception {  
  byte[] keyBytes;  
  keyBytes = (new BASE64Decoder()).decodeBuffer(key);  

  X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
  KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
  PublicKey publicKey = keyFactory.generatePublic(keySpec);  
  return publicKey;  
}  
