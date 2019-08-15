byte[] iv = new byte[16];

SecretKey aesKey = new SecretKeySpec("hex key here", "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));

return cipher.doFinal("32 characters here ...".getBytes());
