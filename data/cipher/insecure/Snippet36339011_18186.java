public InputStream encryptInputStream(InputStream inputStream) throws Exception{
      KeyCipher keyCiper = new KeyCipher();
      String streamContent = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
      Cipher cipher = Cipher.getInstance("Blowfish");
      cipher.init(ENCRYPT_MODE, keyCipher.getSecretSpecKey(), keyCipher.getIvParameterSpec());

     InputStream encryptedStream = new ByteArrayInputStream(encodeToString(cipher.doFinal(streamContent.getBytes("UTF-8")), DEFAULT).getBytes());
    return encryptedStream;
}
