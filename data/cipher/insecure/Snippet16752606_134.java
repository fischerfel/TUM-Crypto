public byte[] decrypt(byte[] enc) throws Exception{
    Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    SecretKeySpec    myKey = new SecretKeySpec(key, "DESede");
    IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
    c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
    byte[] cipherText = c3des.doFinal(enc);
    return cipherText;
}
