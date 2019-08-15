cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
encryptedBytes = cipher.doFinal(plain.getBytes());

return encryptedBytes;
