cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, QtedEncryption.publicKey);
cipherData = cipher.doFinal(password.getBytes());
password = Base64.encodeToString(cipherData, Base64.DEFAULT);
