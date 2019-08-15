byte[] tempByte = Base64.decodeBase64(encryptedString);

Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, keySpec);
byte[] cipherData = cipher.doFinal(tempByte);

String ttt = new String(cipherData ,"UTF-8");
System.out.println(ttt);
