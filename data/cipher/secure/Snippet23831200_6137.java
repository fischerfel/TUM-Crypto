Cipher encrypt=Cipher.getInstance("RSA");
encrypt.init(Cipher.ENCRYPT_MODE, privatekey);
byte[] encryptedMessage=encrypt.doFinal(msg.getBytes());
