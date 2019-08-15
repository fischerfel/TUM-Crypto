   public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception
    {
        byte[] dectyptedText = null;
        try
        {
            // decrypt the text using the private key
            Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC"");
              cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
        }
        catch (Exception e)
        {
            _log.error(e, e);
            throw e;
        }
        return dectyptedText;

    }
