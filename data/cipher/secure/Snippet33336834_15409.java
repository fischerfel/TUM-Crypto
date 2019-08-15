Cipher c1 = Cipher.getInstance("RSA");
c1.init(Cipher.ENCRYPT_MODE, publicKey);
encodedBytes = c1.doFinal(secretMessage.getBytes());
encodedMessage = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

Cipher c2 = Cipher.getInstance("RSA");    
c2.init(Cipher.DECRYPT_MODE, privateKey)      
decodedBytes = Base64.decode(encodedMessage.toByteArray(), Base64.DEFAULT);
decryptedMessage = c2.doFinal(decodedBytes);
