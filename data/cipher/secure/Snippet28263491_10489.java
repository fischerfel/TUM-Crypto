protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String encryptedData = request.getParameter("cipherData");
    String data[] = encryptedData.split(":");

    String encrypted = data[0];     
    String salt = data[1];
    String iv = data[2];
    String password = data[3];

    byte[] saltBytes = hexStringToByteArray(salt);
    byte[] ivBytes = hexStringToByteArray(iv);
    IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);        
    SecretKeySpec sKey = null;
    try {
        sKey = (SecretKeySpec) generateKeyFromPassword(password, saltBytes);
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
    }
    try {
        System.out.println( decrypt( encrypted , sKey ,ivParameterSpec));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 10, 128);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    SecretKey secretKey = keyFactory.generateSecret(keySpec);

    return new SecretKeySpec(secretKey.getEncoded(), "AES");
}

public static byte[] hexStringToByteArray(String s) {

    int len = s.length();
    byte[] data = new byte[len / 2];

    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }

    return data;

}

public static String decrypt(String encryptedData, SecretKeySpec sKey, IvParameterSpec ivParameterSpec) throws Exception { 

    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, sKey, ivParameterSpec);
    byte[] decordedValue = Base64.decodeBase64(encryptedData);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);

    return decryptedValue;
}
