  private byte[] aes256Encode(SecretKey key, IvParameterSpec iv, String message) throws InvalidKeyException,
      InvalidAlgorithmParameterException,
      NoSuchAlgorithmException, NoSuchPaddingException,
      IllegalBlockSizeException, BadPaddingException 
  {
    Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    byte[] encrypted = cipher.doFinal(message.getBytes());
    return encrypted;
  }
