String key = "test@gmail.com";
SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

byte iv[] = SOME_RANDOM_32_BYTES;
IvParameterSpec ivSpec = new IvParameterSpec(iv);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

byte[] encryptedResult = cipher.doFinal(text.getBytes("UTF-8"));
