private static byte[] getPassKey(String pass)
{
    byte[] passKey = pass.getBytes("UTF-8"); //get byte arrays of the given password
    byte[] shaKey = MessageDigest.getInstance("SHA-1").digest(passKey); 
    return Arrays.copyOf(shaKey,24);
}

public static String encrypt(String pass, String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

    Cipher desCipher = Cipher.getInstance("DESede");
    desCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getPassKey(pass), "DESede")); 

    byte[] cipherTextBytes = desCipher.doFinal(plainText.getBytes());
    String encoded = new sun.misc.BASE64Encoder().encode(cipherTextBytes);

    return encoded;
}

public static String decrypt(String pass,String cipherText) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

    byte[] decoded = new sun.misc.BASE64Encoder().decode(cipherText);

    Cipher desCipher = Cipher.getInstance("DESede");
    desCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getPassKey(pass), "DESede"));
    plainText = new String(desCipher.doFinal(decoded));
    return plainText;
}
