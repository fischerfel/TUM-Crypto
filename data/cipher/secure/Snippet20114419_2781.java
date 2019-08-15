public byte[] encrypt(String data, PublicKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
{
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] cipherData = cipher.doFinal(data.getBytes("UTF8"));
    return  org.apache.commons.codec.binary.Base64.encodeBase64(cipherData);

}

public byte[] decrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException 
{
    Cipher cipher = Cipher.getInstance("RSA");
    KeyPair pair=LoadKeyPair("c:", "RSA");
    PrivateKey key=pair.getPrivate();
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] byteCipherText =  org.apache.commons.codec.binary.Base64.decodeBase64(data) ;
    byte[] cipherData = cipher.doFinal(byteCipherText);
    return cipherData; //// use new String(cipherData, "UTF8"); to get the original strings
}
