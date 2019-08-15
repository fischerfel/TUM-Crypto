encrypt = Cipher.getInstance("AES", provider);
encrypt.init(Cipher.ENCRYPT_MODE, key) ;
byte[] encrypted = encrypt.doFinal(plainTxt.getBytes()) ;
