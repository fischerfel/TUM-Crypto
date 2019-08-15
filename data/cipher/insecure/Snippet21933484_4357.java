Cipher cipher=Cipher.getInstance("AES");    
cipher.init(Cipher.ENCRYPT_MODE, key);
enFile=cipher.doFinal(bFile);
