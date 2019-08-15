Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
cipher.init(Cipher.DECRYPT_MODE, secretKey);
decd = cipher.doFinal(strToDecrypt.getBytes("UTF-8"));
