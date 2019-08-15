public Encryption(String key2, FileInputStream fileInputStream,
        FileOutputStream fileOutputStream) {
    this.key = key2;
    this.is = fileInputStream;
    this.os = fileOutputStream;
}

public boolean encryption() throws Throwable {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(key.getBytes());

      SecretKeySpec key = new SecretKeySpec(md5.digest(), "AES");

      cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherInputStream = new CipherInputStream(is, cipher);

    byte[] bytes = new byte[64];
    int numBytes;
    while ((numBytes = cipherInputStream.read(bytes)) != -1) {
        os.write(bytes, 0, numBytes);
    }
    os.flush();
    os.close();
    is.close();
    cipherInputStream.close();
    return true;
}
