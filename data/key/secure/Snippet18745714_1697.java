final SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey, CIPHER_ALGORITHM);

// Instantiate the cipher
final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);

cipher.init(cryptionMode, secretKeySpec, iv);
return cipher;
