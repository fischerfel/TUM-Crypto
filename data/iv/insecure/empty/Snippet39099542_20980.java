public byte[] encrypt(byte[] data, byte[] key) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    byte[] iv = new byte[16];
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    Cipher acipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] arrayOfByte1;
    acipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    arrayOfByte1 = acipher.doFinal(data);
    return arrayOfByte1;
}
