encrypt_ciper = Cipher.getInstance("AES");
decrypt_ciper =   Cipher.getInstance("AES");
text=Base64.encodeToString(encrypt_ciper.doFinal(strToEncrypt.getBytes()),Base64.DEFAULT);
