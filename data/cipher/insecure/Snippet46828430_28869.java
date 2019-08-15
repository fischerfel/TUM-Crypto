String plainText = "Please encrypt me urgently..."
Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
