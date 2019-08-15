 public byte[] keyGen() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(192);
    return keyGenerator.generateKey().getEncoded();
 }

 public byte[] encript(byte[] dataToEncrypt, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    //I'm using AES encription
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    return c.doFinal(dataToEncrypt);
  }

  public byte[] decript(byte[] encryptedData, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(key, "AES");
    c.init(Cipher.DECRYPT_MODE, k);
    return c.doFinal(encryptedData);
  }
