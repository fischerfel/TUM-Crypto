final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);
cipher.doFinal(*something*);
