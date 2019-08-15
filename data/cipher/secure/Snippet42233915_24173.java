SecretKeySpec key = new SecretKeySpec(KEY, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
