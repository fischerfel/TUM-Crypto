String key = enc_key;

// Create key and cipher
Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");

// decrypt the text
cipher.init(Cipher.DECRYPT_MODE, aesKey);
strDecrypted = new String(cipher.doFinal(Base64.decodeBase64(encrypted.getBytes())));
