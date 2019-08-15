// Get an instance of the Cipher for RSA encryption/decryption
Cipher c = Cipher.getInstance("RSA");
// Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
c.init(Cipher.ENCRYPT_MODE, myPair.getPublic()); 
