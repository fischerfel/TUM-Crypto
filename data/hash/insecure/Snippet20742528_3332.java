    byte[] message = new BASE64Decoder().decodeBuffer(encryptedText);
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] digestOfPassword = md.digest(kEY.getBytes("UTF-16LE"));
    byte[] keyBytes = new byte[24];
    System.arraycopy(digestOfPassword, 0, keyBytes, 0, 16);
    System.arraycopy(digestOfPassword, 0, keyBytes, 16, 8);
    SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    decipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainText = decipher.doFinal(message);
    String decryptedString = new String(plainText, UTF);

}
