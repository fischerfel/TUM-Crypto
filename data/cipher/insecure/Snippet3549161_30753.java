Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, getAES128SecretKey());
byte[] encrypted = cipher.doFinal(input);
