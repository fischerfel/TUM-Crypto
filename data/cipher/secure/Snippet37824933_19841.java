Cipher ciphertest = null;
ciphertest = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
ciphertest.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encryptedBytes = null;
encryptedBytes = ciphertest.doFinal(Bytes_to_encrypt);
