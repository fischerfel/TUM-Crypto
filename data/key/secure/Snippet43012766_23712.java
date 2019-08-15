try (CloseableKey key = 
       new CloseableKey(new SecretKeySpec(data, 0, 16, "AES"))) {
  aesecb.init(Cipher.ENCRYPT_MODE, key.secretKeySpec);
}
