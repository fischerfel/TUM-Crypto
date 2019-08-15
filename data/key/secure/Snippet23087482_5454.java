SecretKeySpec key = new SecretKeySpec(cryptPassword.getBytes(), "AES");

        byte[] cryptByte = cryptString.getBytes("UTF8"); 

        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] hans = cipher.doFinal(cryptByte);


        cryptString = Base64.encodeToString(hans,Base64.DEFAULT);
