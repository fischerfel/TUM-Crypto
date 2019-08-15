public static String encrypt(byte[] textBytes, KeyPair pair, Cipher rsaCipher) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    //get the public key
    PublicKey pk=pair.getPublic(); 


    //Initialize the cipher for encryption. Use the public key.
    rsaCipher.init(Cipher.ENCRYPT_MODE, pk);

    //Perform the encryption using doFinal
    byte[] encByte = rsaCipher.doFinal(textBytes);

    // converts to base64 for easier display.
    byte[] base64Cipher = Base64.encodeBase64(encByte);

    return new String(base64Cipher);
}//end encrypt

public static String decrypt(byte[] cipherBytes, KeyPair pair, Cipher rsaCipher) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException
{
    //get the public key
    PrivateKey pvk=pair.getPrivate(); 

    //Create a Cipher object
    //Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");

    //Initialize the cipher for encryption. Use the public key.
    rsaCipher.init(Cipher.DECRYPT_MODE, pvk);

    //Perform the encryption using doFinal
    byte[] decByte = rsaCipher.doFinal(cipherBytes);

    return new String(decByte);

}//end decrypt
