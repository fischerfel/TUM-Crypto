public static String decrypt(byte[] cipherText) throws Exception{
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
      SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
      cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
      return new String(cipher.doFinal(cipherText),"UTF-8");
}
