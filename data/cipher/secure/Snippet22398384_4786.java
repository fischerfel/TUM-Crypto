private static byte[] decrypt(byte[] message, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException
{
Cipher cipher = Cipher.getInstance(ALGORITHEM);
cipher.init(Cipher.DECRYPT_MODE, key);

return cipher.doFinal(message);
}
