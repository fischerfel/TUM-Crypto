SecretKeySpec sks = null; // Or, equivalently SecretKey sk = null;
        SecretKey sk =null;
        try {
            KeyStore keyStore= KeyStore.getInstance(KeyStore.getDefaultType());
            char[] passwordKS="network".toCharArray();
            FileInputStream fis =null;
            try
             {
                 fis = openFileInput("keyStoreName");
             }catch (Exception ex)
            {
                }
            keyStore.load(fis,passwordKS);
            //sk=(SecretKey) keyStore.getKey("aliasKey", passwordKS);
             sk=(SecretKey) keyStore.getKey("aliasKey", passwordKS);
            sks=new SecretKeySpec((keyStore.getKey("aliasKey", passwordKS)).getEncoded(), "AES");
           } catch (Exception e) {
           }
        byte[] latDEC=null;
        byte[] longDEC=null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            latDEC = c.doFinal(lat.getBytes());
            longDEC = c.doFinal(longit.getBytes());
        } catch (Exception e) {
           }
