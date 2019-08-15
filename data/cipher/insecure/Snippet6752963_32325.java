byte[] keyBytes = ...
byte[] volumeKeyFileContents = ...

SecretKeyFactory factory = SecretKeyFactory.getInstance("AES");
SecretKey aesKey = factory.generateSecret(new SecretKeySpec(keyBytes, "AES"));

Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // for example
cipher.init(Cipher.DECRYPT_MODE, aesKey);
byte[] plaintext = cipher.doFinal(volumeKeyFileContents);

// (written from memory so may not compile without tweaks)
