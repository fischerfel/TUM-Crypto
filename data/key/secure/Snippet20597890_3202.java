// Turn the encoded key into a real RSA private key.
// Private keys are encoded in PKCS#8.
PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

// Create a cipher using that key to initialize it
Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

// Read in the encrypted bytes of the session key
DataInputStream dis = new DataInputStream(new FileInputStream(fileInput));
byte[] encryptedKeyBytes = new byte[dis.readInt()];
dis.readFully(encryptedKeyBytes);

// Decrypt the session key bytes.
rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] rijndaelKeyBytes = rsaCipher.doFinal(encryptedKeyBytes);

// Transform the key bytes into an actual key.
SecretKey rijndaelKey = new SecretKeySpec(rijndaelKeyBytes, "Rijndael");
