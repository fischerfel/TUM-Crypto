private static char[] base64Encode(byte[] bytes) 
{   
    return Base64.encode(bytes);
}

private static String encrypt(String encrypt_this) throws GeneralSecurityException, UnsupportedEncodingException 
{
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

     //THIS FAILED when attempting to decrypt the password
    //return base64Encode(pbeCipher.doFinal(encrypt_this.getBytes("UTF-8"))).toString(); 

    //THIS WORKED
    return String.valueOf(base64Encode(pbeCipher.doFinal(encrypt_this.getBytes("UTF-8"))));
}//end of encrypt()
