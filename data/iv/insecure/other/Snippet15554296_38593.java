@Test
public void testEncrypt() {
  try {
    String s = "Hello there. How are you? Have a nice day.";

    // Generate key
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);
    SecretKey aesKey = kgen.generateKey();

    // Encrypt cipher
    Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);

    // Encrypt
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
    cipherOutputStream.write(s.getBytes());
    cipherOutputStream.flush();
    cipherOutputStream.close();
    byte[] encryptedBytes = outputStream.toByteArray();

    // Decrypt cipher
    Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
    decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);

    // Decrypt
    outputStream = new ByteArrayOutputStream();
    ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
    CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
    byte[] buf = new byte[1024];
    int bytesRead;
    while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
        outputStream.write(buf, 0, bytesRead);
    }

    System.out.println("Result: " + new String(outputStream.toByteArray()));

  } 
  catch (Exception ex) {
    ex.printStackTrace();
  }
}
