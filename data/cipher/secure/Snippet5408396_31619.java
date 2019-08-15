Cipher cipher2 = Cipher.getInstance("RSA");
cipher2.init(Cipher.DECRYPT_MODE, priKey);
byte[] cipherData = cipher2.doFinal(data);
