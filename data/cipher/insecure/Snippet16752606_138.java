Cipher c3des = Cipher.getInstance("DESede/CBC/NoPadding");
c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
byte[] cipherText = c3des.doFinal(enc);
