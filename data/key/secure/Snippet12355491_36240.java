byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00 };
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
SecretKey s = new SecretKeySpec(key, Algorithm.TEDE.toString());
Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding", "BC");
cipher.init(Cipher.DECRYPT_MODE, s, ivParameterSpec);
byte[] deciphered_data = cipher.doFinal(enciphered_data);
