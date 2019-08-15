public static byte[] decrypt(byte [] encryptedByteArray,String keyString)throws NoSuchAlgorithmException, 
                                                            NoSuchPaddingException, InvalidKeyException,
                                                            IllegalBlockSizeException, BadPaddingException
{
    SecretKey key=loadKey(keyString);

byte[] clearByteArray;

Cipher dCipher=Cipher.getInstance("AES");
dCipher.init(Cipher.DECRYPT_MODE,key );
clearByteArray=dCipher.doFinal(encryptedByteArray);
return clearByteArray;
