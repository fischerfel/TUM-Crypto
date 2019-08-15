// Encryption:
String message = "HELLO";
Cipher rsa = Cipher.getInstance("RSA");
rsa.init(Cipher.ENCRYPT_MODE, privateKey);  // privateKey has type java.security.PrivateKey
byte [] encryptedBytes = rsa.doFinal(message.getBytes());

// Decryption:
rsa.init(Cipher.DECRYPT_MODE, publicKey); // type of publicKey: java.security.PublicKey
byte [] ciphertext = rsa.doFinal(encryptedBytes);

String decryptedString = new String(ciphertext, "UTF-8");
