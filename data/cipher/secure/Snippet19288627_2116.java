 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
 cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
