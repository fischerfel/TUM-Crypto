KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
keyGenerator.init(128, new SecureRandom());
SecretKey secretKey = keyGenerator.generateKey();

Cipher cipher = Cipher.getInstance("AES");

String plainText = "This is supposed to be encrypted";
String plainKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);

//encrypt
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
String encryptedText = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

//decrypt
cipher.init(Cipher.DECRYPT_MODE, secretKey);
byte[]decryptedBytes = cipher.doFinal(encryptedBytes);
String decryptedText = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
