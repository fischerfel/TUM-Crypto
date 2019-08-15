byte[] key = getKey();
Cipher cipher = Cipher.getInstance("Blowfish");
SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
cipher.init(Cipher.DECRYPT_MODE, keySpec);
