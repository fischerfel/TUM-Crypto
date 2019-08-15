private Cipher readKey(InputStream re) throws Exception {
    byte[] encodedKey = new byte[decryptBuferSize];
    re.read(encodedKey); //Check the return value of the "read" call to see how many bytes were read. (the issue I get from Sonar)


    byte[] key = keyDcipher.doFinal(encodedKey);
    Cipher dcipher = ConverterUtils.getAesCipher();
    dcipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
    return dcipher;
}
