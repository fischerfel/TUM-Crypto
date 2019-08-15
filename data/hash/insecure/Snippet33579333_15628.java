public static String Encrypt(String plainText, byte[] key2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
      byte[] encryptedTextBytes=null;
      byte[] key3 =null;
      MessageDigest sha = MessageDigest.getInstance("SHA-1");
      key3= sha.digest(key2);
      key3 = copyOf(key3, 16);
      SecretKeySpec keySpec = new SecretKeySpec(key3, "AES");
      // Instantiate the cipher
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      encryptedTextBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
      return new Base64().encode(encryptedTextBytes);
}
