Cipher c = Cipher.getInstance("RSA");
c.init(Cipher.DECRYPT_MODE,_clientPrivateKey);
byte [] r = c..doFinal(encrypted);
