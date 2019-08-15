// aesKey is 128, 196 or 256-bit key (8, 12 or 16 byte array)
SecretKeySpec key = new SecretKeySpec(aesKey, "AES");

// initialization vector
IvParameterSpec ivSpec = new IvParameterSpec(iv);

// create and initialize cipher object
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
