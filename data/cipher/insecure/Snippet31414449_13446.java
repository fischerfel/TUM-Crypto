Cipher cipher = Cipher.getInstance("Rijndael/ECB/NoPadding");
byte[] key = getKey(input[1]);
SecretKey secretKey = new SecretKeySpec(key, 0, key.length, "Rijndael");
cipher.init(Cipher.DECRYPT_MODE, secretKey);
