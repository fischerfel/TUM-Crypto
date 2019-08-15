String iv = "0000000000000000";
IvParameterSpec ivspec;
ivspec = new IvParameterSpec(iv.getBytes());

byte[] decrypted = null;

Cipher cipher;
cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, seckey, ivspec);
decrypted = cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT));
Log.i("Decrypted Data",decrypted.toString());
