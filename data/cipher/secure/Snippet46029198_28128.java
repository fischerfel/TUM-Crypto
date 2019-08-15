Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
SecretKey skey = new SecretKeySpec(key, 0, key.length, "AES");
