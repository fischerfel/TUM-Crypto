Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, skey);
byte[] encrypted = cipher.doFinal(plainText.getBytes());
