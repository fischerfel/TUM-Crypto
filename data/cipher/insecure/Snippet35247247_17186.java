private String decrypt(String encrypted) throws Exception {
    Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
    //byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted));

    /*this line throw an exception*/                                      
    byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));

    return new String(plainBytes);
}

private Cipher getCipher(int cipherMode) throws Exception {
    String encryptionAlgorithm = "AES";
    SecretKeySpec keySpecification = new SecretKeySpec(
            encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
    Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
    cipher.init(cipherMode, keySpecification);

    return cipher;
}




Exception in thread "main" java.lang.NullPointerException
at java.util.Base64$Decoder.decode(Base64.java:549)
at edmaker.EdMaker.decrypt(EdMaker.java:148)
