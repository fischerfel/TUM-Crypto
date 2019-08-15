SecretKeySpec key = new SecretKeySpec(cipher_key, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(salt, 0, HALF_BLOCK / 8);
Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
