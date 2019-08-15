SecretKeySpec skeySpec = new SecretKeySpec(getCryptoKeyByteArray(length=16)); // 128 bits
Cipher encryptor = Cipher.getInstance("AES");
encryptor.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] encrypted = encryptor.doFinal(plain);
