 public String Decrypt (String result) throws NoSuchAlgorithmException,          
 NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, 
 BadPaddingException
{

    cipher1= Cipher.getInstance("RSA");
    cipher1.init(Cipher.DECRYPT_MODE, privateKey);
    decryptedBytes = cipher1.doFinal(stringToBytes(result));
    decrypted = new String(decryptedBytes);
    return decrypted;

}
