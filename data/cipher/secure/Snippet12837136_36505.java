// decode the key string into bytes (using Apache Commons)
byte[] keyBytes = Hex.decodeHex(keystr.toCharArray());

// create a representation of the key
SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");

// turn the key spec into a usable key
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
SecretKey key = keyFactory.generateSecret(spec);

// use a cipher to decrypt the eid
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] plainText = cipher.doFinal(hex.decodeHex(eid.toCharArray())); // decode from Hex again
