private static byte[] encryptGCM(byte[] plaintext,
        byte[] randomKeyBytes, byte[] randomIvBytes) throws Exception{
    SecretKey randomKey = new SecretKeySpec(randomKeyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", new BouncyCastleProvider());
    cipher.init(Cipher.ENCRYPT_MODE, randomKey, new IvParameterSpec(
            randomIvBytes));    //TODO: here IvParamSpec could also be gcmP   = new GCMParameterSpec(12, keys, 32, 12); 

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
    cipherOutputStream.write(plaintext);
    cipherOutputStream.close();
    return byteArrayOutputStream.toByteArray();//this is the encrypted text
}
