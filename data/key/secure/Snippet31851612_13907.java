class Encryptor {
  Key key;

  Encryptor(byte[] key) {
    if (key.length != 32) throw new IllegalArgumentException();
    this.key = new SecretKeySpec(key, "AES");
  }

  // the output is sent to users
  byte[] encrypt(byte[] src) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] iv = cipher.getIV(); // See question #1
    assert iv.length == 12; // See question #2
    byte[] cipherText = cipher.doFinal(src);
    assert cipherText.length == src.length + 16; // See question #3
    byte[] message = new byte[12 + src.length + 16]; // See question #4
    System.arraycopy(iv, 0, message, 0, 12);
    System.arraycopy(cipherText, 0, message, 12, cipherText.length);
    return message;
  }

  // the input comes from users
  byte[] decrypt(byte[] message) throws Exception {
    if (message.length < 12 + 16) throw new IllegalArgumentException();
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec params = new GCMParameterSpec(128, message, 0, 12);
    cipher.init(Cipher.DECRYPT_MODE, key, params);
    return cipher.doFinal(message, 12, message.length - 12);
  }
}
