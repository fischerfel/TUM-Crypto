public static byte[] decrypt(byte[] encryptedData) throws Exception {
    Key key = new SecretKeySpec(keyBytes, "AES");
    Cipher c = Cipher.getInstance("AES/CTR/NoPadding"); 

    byte[] iv = Arrays.copyOfRange(encryptedData, 0, 16) ; // first 16 bytes
    byte[] data = Arrays.copyOfRange(encryptedData, 16, 1024); // rest

    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    c.init(Cipher.DECRYPT_MODE, key, ivSpec);
    byte[] decValue = c.doFinal(data);
    return decValue;
}
