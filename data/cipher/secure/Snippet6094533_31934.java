Cipher wrapper = Cipher.getInstance("RSA", "BC");
wrapper.init(Cipher.ENCRYPT_MODE, publicKey);
encryptedData= wrapper.doFinal(unencryptedData);
