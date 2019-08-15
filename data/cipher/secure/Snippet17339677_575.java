private static byte[] INITIALIZATION_VECTOR = new byte[] { 0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00 };
public static String encrypt(String token) {
    Cipher cipher = null;
    SecretKey key = null;
    String tokenAsHex = null;
    byte[] encryptedToken = null;
    byte[] sksKey = "6iOmT2V6mnd0".getBytes(); // SecretKeySpec key.

    try {
        key = new SecretKeySpec(sksKey, "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(INITIALIZATION_VECTOR);
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        encryptedToken = cipher.doFinal(token.getBytes("UTF-8"));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return Base64.encodeBase64String(encryptedToken);
}

public static String decrypt(String token) {
    Cipher cipher = null;
    SecretKey key = null;
    byte[] decryptedToken = null;
    byte[] sksKey = "6iOmT2V6mnd0".getBytes(); // SecretKeySpec key.
    try {
        key = new SecretKeySpec(sksKey, "AES");            
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(INITIALIZATION_VECTOR);
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        decryptedToken = cipher.doFinal(Base64.decodeBase64(token));
    } catch(Exception e){
        e.printStackTrace();    
    }
    if (decryptedToken == null) {
         System.out.println("Unable to decrypt the following token: " + token);
    }
    return new String(decryptedToken);
}
