Cipher cipher2 = Cipher.getInstance("RSA");
cipher2.init(Cipher.DECRYPT_MODE, privateKey);
byte[] plainData = cipher.doFinal(cipherData);
String p  = new String(plainData);
Log.d("decrypted data is:",p);
