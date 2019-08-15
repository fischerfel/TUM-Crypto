Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
byte[] cipherText = ...;
byte[] plainText = cipher.doFinal(cipherText);
