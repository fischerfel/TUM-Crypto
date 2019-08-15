byte[] IV = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "BC");

cipher.init(Cipher.ENCRYPT_MODE, key, IV);

byte[] result = cipher.doFinal("Some plaintext");
