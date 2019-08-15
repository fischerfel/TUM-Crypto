SecretKey key = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
plaintext = new String(cipher.doFinal(msg), "UTF-8"); 
