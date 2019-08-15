IvParameterSpec salt = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
