    public void testSymCypher(SecretKey k, String str)
        throws BadPaddingException, IllegalBlockSizeException,
        InvalidAlgorithmParameterException, InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException
{
    Cipher cip = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cip.init(Cipher.ENCRYPT_MODE,k);
    byte[] ciphered = cip.doFinal(str.getBytes());
    byte iv[] = cip.getIV();

    // printing the ciphered string
    printHexadecimal(ciphered);

    IvParameterSpec dps = new IvParameterSpec(iv);
    cip.init(Cipher.DECRYPT_MODE,k,dps);
    byte[] deciphered = cip.doFinal(ciphered);

    // printing the deciphered string
    printHexadecimal(deciphered);
}
