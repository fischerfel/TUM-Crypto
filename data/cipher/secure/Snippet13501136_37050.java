// Get an instance of the Cipher for RSA encryption/decryption
Cipher dec = Cipher.getInstance("RSA");
// Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
dec.init(Cipher.DECRYPT_MODE, myPair.getPrivate());
