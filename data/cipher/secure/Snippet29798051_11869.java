 public static String encrytData(String text) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    byte[] static_key = Constants.AES_KEY.getBytes();

    SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV_VECTOR);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    byte[] results = cipher.doFinal(text.getBytes());

    String result = Base64.encodeToString(results, Base64.NO_WRAP|Base64.DEFAULT);
    return result;

}


public static String decryptData(String text)throws Exception{

    byte[] encryted_bytes = Base64.decode(text, Base64.DEFAULT);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    byte[] static_key = Constants.AES_KEY.getBytes();

    SecretKeySpec keySpec = new SecretKeySpec(static_key, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(Constants.IV_VECTOR);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    byte[] decrypted = cipher.doFinal(encryted_bytes);
    String result = new String(decrypted);

    return result;
}
