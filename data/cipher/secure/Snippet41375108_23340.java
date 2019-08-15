Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] data = cipher.doFinal(message); 
