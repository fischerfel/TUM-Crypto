Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "BC");

cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] result = cipher.doFinal("Some plaintext");
