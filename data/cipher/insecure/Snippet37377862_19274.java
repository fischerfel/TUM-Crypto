public static String encryptText(String plainText) throws Exception {

    byte[] plaintext = plainText.getBytes();//input
    byte[] tdesKeyData = Constants.getKey().getBytes();// your encryption key

    byte[] myIV = Constants.getInitializationVector().getBytes();// initialization vector

    Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(myIV);

    c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
    byte[] cipherText = c3des.doFinal(plaintext);
    String encryptedString = Base64.encodeToString(cipherText,
            Base64.DEFAULT);
    // return Base64Coder.encodeString(new String(cipherText));
    return encryptedString;
}

private static class Constants
{
    private static final String KEY="";
    private static final String INITIALIZATION_VECTOR="";
    public static String getKey()
    {
        return KEY;
    }


    public static String getInitializationVector()
    {
        return INITIALIZATION_VECTOR;
    }
}
