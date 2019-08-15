String secret = "1234567812345678";
Key key = new SecretKeySpec(secret.getBytes(), "AES");

// Encrypt
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] encryptedData = cipher.doFinal("helloworld".getBytes());

// Decrypt
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] decryptedData = cipher.doFinal(encryptedData);
System.out.println(new String(decryptedData, "UTF-8"));
