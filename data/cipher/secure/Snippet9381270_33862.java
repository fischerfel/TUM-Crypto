cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");        
cipher.init(Cipher.ENCRYPT_MODE, aesKey);
byte[] data = cipher.doFinal(stringDec.getBytes());
byte[] iv = cipher.getIV();
