private static String Key = "your key"; 
public static String encryptString(String stringToEncode) throws NullPointerException {

    try {
        SecretKeySpec skeySpec = getKey(Key);
        byte[] clearText = stringToEncode.getBytes("UTF8");
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
        return encrypedValue;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}
public static String encryptString(String stringToEncode) throws NullPointerException {

    try {
        SecretKeySpec skeySpec = getKey(Key);
        byte[] clearText = stringToEncode.getBytes("UTF8");
        final byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] cipherData = cipher.doFinal(Base64.decode(stringToEncode.getBytes("UTF-8"), Base64.DEFAULT));
        String decoded = new String(cipherData, "UTF-8");
        return decoded;

    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}


private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
    int keyLength = 256;
    byte[] keyBytes = new byte[keyLength / 8];
    Arrays.fill(keyBytes, (byte) 0x0);
    byte[] passwordBytes = password.getBytes("UTF-8");
    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    return key;
}
