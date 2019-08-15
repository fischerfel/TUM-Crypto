public static String AES_Encode(String str, String key) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  

    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    String encryptedString = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));

    return encryptedString;


}

public static String AES_Decode(String str, String key) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(str)),"UTF-8");

    return decryptedString;

}
