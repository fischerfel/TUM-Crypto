String saltD = text.substring(0,12);
String ciphertext = text.substring(12,text.length());

// BASE64Decode the bytes for the salt and the ciphertext
Base64 decoder = new Base64();
byte[] saltArray = decoder.decode(saltD);
byte[] ciphertextArray = decoder.decode(ciphertext);

// Create the PBEKeySpec with the given password
PBEKeySpec keySpec = new PBEKeySpec(password.trim().toCharArray());

// Get a SecretKeyFactory for PBEWithSHAAndTwofish
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionMethod);

// Create our key
SecretKey key = keyFactory.generateSecret(keySpec);

// Now create a parameter spec for our salt and iterations
PBEParameterSpec paramSpec = new PBEParameterSpec(saltArray, ITERATIONS);

// Create a cipher and initialize it for encrypting
Cipher cipher = Cipher.getInstance(encryptionMethod);
cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

// Perform the actual decryption
byte[] plaintextArray = cipher.doFinal(ciphertextArray);
return new String(plaintextArray);
