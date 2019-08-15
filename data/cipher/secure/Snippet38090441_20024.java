    try
    {

        Cipher cipherb = Cipher.getInstance("RSA/PKCS1", "BC");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        ...
    }       
    catch(Exception e)
    {
        e.printStackTrace();
        return ;
    }
