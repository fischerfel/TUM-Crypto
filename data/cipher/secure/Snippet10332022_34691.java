Cipher c = Cipher.getInstance("RSA/NONE/NoPadding", "SC");
c.init(Cipher.DECRYPT_MODE, pubKey);
byte[] result = c.doFinal(data_to_decrypt.getBytes());
