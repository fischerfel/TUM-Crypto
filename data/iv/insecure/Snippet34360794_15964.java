byte[] sessionKey = Base64.encodeBase64("my_salt".getBytes());
byte[] iv = Base64.encodeBase64("WHAT-GOES-HERE??".getBytes());
byte[] plaintext = rawText.getBytes();
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
byte[] ciphertext = cipher.doFinal(plaintext);
return new String(ciphertext, "UTF8");
