String password = "some value";
byte[] passwordBytes = password.getBytes(); 
Cipher cipher = Cipher.getInstance("AES"); 
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(passwordBytes, "AES")); 
byte[] encryptedBytes = cipher.doFinal(cursor.toWebSafeString().getBytes());
