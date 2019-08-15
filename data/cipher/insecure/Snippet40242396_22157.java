Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding"); 
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] result = cipher.doFinal(data);
