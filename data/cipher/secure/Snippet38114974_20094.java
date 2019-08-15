Cipher cipherb = Cipher.getInstance("RSA/NONE/NoPadding");
cipherb.init(Cipher.DECRYPT_MODE, publicKey);
decrypted = cipherb.doFinal(text.getBytes());
