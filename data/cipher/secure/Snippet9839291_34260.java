Cipher rsaCipher = Cipher.getInstance("RSA");
rsaCipher.init(Cipher.DECRYPT_MODE, key);
byte[] b = rsaCipher.doFinal('symkey'.getbytes());
