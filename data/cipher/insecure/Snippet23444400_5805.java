 String key = "Bar12345Bar12345"; // 128 bit key
 // Create key and cipher
 Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
 Cipher cipher = Cipher.getInstance("AES");
