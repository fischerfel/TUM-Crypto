KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
keyGenerator.init(256);
SecretKey secretKey = keyGenerator.generateKey();
Cipher decryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");
decryption.init(Cipher.DECRYPT_MODE, secretKey, 
new IvParameterSpec(secretKey.getEncoded())); // <-- Illegal key size
