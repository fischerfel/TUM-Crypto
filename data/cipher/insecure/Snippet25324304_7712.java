  Cipher rc4Decrypt = Cipher.getInstance("RC4");
  rc4Decrypt.init(Cipher.DECRYPT_MODE, rc4Key);
  byte [] clearText2 = rc4Decrypt.update(cipherText);
