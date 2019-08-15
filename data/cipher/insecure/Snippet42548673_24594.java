KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
        SecretKey myDesKey = keygenerator.generateKey();
        Cipher desCipher;
        desCipher = Cipher.getInstance("DES");

       desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
       byte[] text =uname.getBytes();
       byte[] textEncrypted = desCipher.doFinal(text);
       System.out.println("username Encryted : " + textEncrypted);
