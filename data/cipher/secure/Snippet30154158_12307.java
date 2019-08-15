  Cipher cipher;
  cipher = Cipher.getInstance("AES/CBC/NoPadding");
  cipher.init(Cipher.DECRYPT_MODE, seckey, ivspec);
  decrypted = cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT)); //byte[]
  String result = new String(decrypted);
  Log.i("Decrypted Data",result);
