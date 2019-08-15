byte[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
SecretKey keySpec = new SecretKeySpec(key, "Blowfish");
Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
