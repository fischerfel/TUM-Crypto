Cipher c = Cipher.getInstance("RSA");
c.init(Cipher.ENCRYPT_MODE,senderKey); 
SealedObject myEncryptMessage = new SealedObject( encryptionParameter, c);
