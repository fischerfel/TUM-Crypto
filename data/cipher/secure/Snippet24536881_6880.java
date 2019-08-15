public static String encode(String keyString, String stringToEncode){
    SecretKeySpec skeySpec = getKey(keyString);
    byte[] clearText = stringToEncode.getBytes("UTF-8");            
    // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
    final byte[] iv = new byte[16];
    Arrays.fill(iv, (byte) 0x00);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);          
    // Cipher is not thread safe
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);            
    String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.NO_WRAP);
    return encrypedValue;
}


private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
    int keyLength = 256;
    byte[] keyBytes = new byte[keyLength / 8];
    // explicitly fill with zeros
    Arrays.fill(keyBytes, (byte) 0x0);
    // if password is shorter then key length, it will be zero-padded to key length
    byte[] passwordBytes = password.getBytes("UTF-8");
    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    return key;
}
