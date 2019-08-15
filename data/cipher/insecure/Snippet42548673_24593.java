 KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
             SecretKey myDesKey = keygenerator.generateKey();
             Cipher desCipher;
             desCipher = Cipher.getInstance("DES");

        /*  **String uname=request.getParameter("uname");
            System.out.println("username from link" +uname); */**

            byte[] ci=uname.getBytes();
            System.out.println("byte[] ci"+ci);
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
             byte[] textDecrypted = desCipher.doFinal(ci);
             System.out.println("Text Decryted : " + new String(textDecrypted));
