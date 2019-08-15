// specify mode and padding instead of relying on defaults (use OAEP if available!)
Cipher encrypt=Cipher.getInstance("RSA/ECB/PKCS1Padding");
// init with the *public key*!
encrypt.init(Cipher.ENCRYPT_MODE, publicKey);
// encrypt with known character encoding, you should probably use hybrid cryptography instead 
byte[] encryptedMessage = encrypt.doFinal(msg.getBytes(StandardCharsets.UTF_8));
