public static String encrypt(String plaintxt)            //line 24
    {
        String encryptedtxt = null;
        try
        {
            Key skey=aes.setkey(pswd);
            Cipher c=Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, skey);
            byte[] P=plaintxt.getBytes("UTF-8");
            encryptedtxt=new String(c.doFinal(P));
        }catch(Exception e){
            e.printStackTrace();
        }
        return encryptedtxt;
    }
