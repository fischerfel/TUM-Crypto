final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),
        "AES");
        final String myIV = "89ABCDEF01234567"; 
        Cipher c = null;
        try {
            try {
                c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(
                    myIV.getBytes()));


            byte[] encrypted = c.doFinal(msgfromEB.getBytes(),0,msgfromEB.getBytes().length);
