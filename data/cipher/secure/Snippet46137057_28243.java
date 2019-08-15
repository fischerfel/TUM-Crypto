public String Decrypt (String result,String privKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    PrivateKey privateKey = getPrivateKeyFromString(privKey);
    Cipher cipher1 = Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    String decrypted="";
    try {
        byte[] bytes = hexStringToByteArray(result);
        byte[] decryptedBytes = cipher1.doFinal(bytes);
         decrypted = new String(decryptedBytes);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    return decrypted;

}

public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len/2];

    for(int i = 0; i < len; i+=2){
        data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
    }

    return data;
}
