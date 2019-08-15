// 1. Generate a session key
KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(128)
SecretKey sessionKey = keyGen.generateKey();

// 2. Encrypt the session key with the RSA public key
Cipher rsaCipher = Cipher.getInstance("RSA");
rsaCipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());

// 3. Encrypt the data using the session key (unencrypted)
Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
aesCipher.init(Cipher.ENCRYPT_MODE, sessionKey); <-- sessionKey is the unencrypted
//                                                   session key.
// ... use aesCipher to encrypt your data

// 4. Save the encrypted data along with the encrypted 
// session key (encryptedSessionKey).
// PLEASE NOTE THAT BECAUSE OF THE ENCRYPTION MODE (CBC),
// YOU ALSO NEED TO ALSO SAVE THE IV (INITIALIZATION VECTOR).
// aesCipher.aesCipher.getParameters().
//     getParametersSpec(IvParameters.class).getIV();
