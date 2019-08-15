/**
 * This method is used to encrypt a string value.
 * 
 * @param text
 *          - string value to be encrypted.
 *          
 * @return result(encrypted string) as String
 * 
 * @throws Exception
 * 
 */
@TargetApi(8)
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

/**
 * This method is used to decrypt a string value.
 * 
 * @param text
 *          - string value to be decrypted.
 * @return result(decrypted string) as String
 * 
 * @throws Exception
 */
@SuppressLint("NewApi")
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
