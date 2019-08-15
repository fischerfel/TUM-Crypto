 public static String encryptStringToBase64(String messageString) { 
        byte[] messageBytes = messageString.getBytes("UTF-8"); 
        byte[] encrypted = convert(1, messageBytes); 
        return Base64.encodeBytes(encrypted); 
    } 

private static byte[] convert(int mode, byte[] messageBytes) { 

    MessageDigest sha256 = MessageDigest.getInstance("SHA-256"); 
    sha256.update("abcdefgh".getBytes("UTF-8")); 
    byte[] keyBytes = sha256.digest(); 
    byte[] hash = Arrays.copyOfRange(keyBytes, 0, 16); 

    SecretKeySpec keySpec = new SecretKeySpec(hash, "AES"); 
    byte[] ivBytes = new byte[16]; 
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes); 
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
    cipher.init(mode, keySpec, ivSpec); 
    return cipher.doFinal(messageBytes); 
}
