Cipher rsaCipher = Cipher.getInstance("RSA/None/PKCS1Padding", "SC");
rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
byte[] ENCRYPTED_YOUR_STRING = rsaCipher.doFinal(YOUR_STRING);
