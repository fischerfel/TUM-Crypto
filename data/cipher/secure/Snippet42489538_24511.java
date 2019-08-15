public static BigInteger encrypt(BigInteger bg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
    byte[] encoded;
    Cipher cipher=Cipher.getInstance("RSA/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, pub);
    encoded=cipher.doFinal(bg.toByteArray());
    // always return a positive number, all numbers in RSA range from 0 to N, the modulus
    return new BigInteger(1, encoded); // <-- CHANGED
}
