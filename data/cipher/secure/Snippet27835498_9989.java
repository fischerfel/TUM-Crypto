private  String RSA_Decryption(String encryptedData) 
        throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,     IOException, IllegalBlockSizeException, BadPaddingException, GeneralSecurityException 
{
    BigInteger m = new BigInteger("verylongnumber1");
    BigInteger e = new BigInteger("verylongnumber2");

    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = fact.generatePrivate(keySpec);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
    byte[] decValue = cipher.doFinal(decordedValue);
    String decryptedValue = new String(decValue, "UTF-8");

    return decryptedValue;     
}
