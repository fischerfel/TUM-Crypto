public Decryption(String decodingKey, File fileOutput) {
    this.key = decodingKey;
    this.outputFile = fileOutput.getAbsolutePath();
}

public boolean decryption() throws IOException, Throwable {
    encryptedFile = new File("Crypto\\EncryptedFile.txt");
    is = new FileInputStream(encryptedFile);
    File outputF = new File(outputFile);
    os = new FileOutputStream(outputF);

    MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(key.getBytes());

      SecretKeySpec key = new SecretKeySpec(md5.digest(), "AES");

      cipher = Cipher.getInstance("AES");
      //cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.DECRYPT_MODE, key);
    cipherInputStream = new CipherInputStream(is, cipher);

    byte[] bytes = new byte[64];
    int numBytes;
    while ((numBytes = cipherInputStream.read(bytes)) != -1) {
        os.write(bytes, 0, numBytes);
    }
    os.flush();
    os.close();
    cipherInputStream.close();
    is.close();
    encryptedFile.delete();
    return true;
}  
