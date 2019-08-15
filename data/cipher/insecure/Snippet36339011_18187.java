public InputStream decryptInputStream(InputStream inputStream) throws Exception{
    KeyCipher keyCipher = new keyCipher();
    String streamContents = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
    byte[] encrypted = Base64.decode(streamContents, DEFAULT);

    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.DECRYPT_MODE, keyCipher.getSecretSpecKey(), keyCipher.getIvParameterSpec());

    byte[] decryptedBytes = cipher.doFinal(encrypted);
    InputStream decryptedStream = new ByteArrayInputStream(decryptedBytes);
    return decryptedStream;
