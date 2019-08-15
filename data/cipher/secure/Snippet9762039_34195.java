private SecretKey decryptAESKey(byte[] data )
    {
        SecretKey key = null;
        PrivateKey privKey = null;
        Cipher cipher = null;

        try
        {
            // this is OUR private key
            privKey = (PrivateKey)utility.loadLocalKey(
                                    ConfigFrame.privateKeyLocation, false);

            // initialize the cipher...
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey );

            // generate the aes key!
            key = new SecretKeySpec ( cipher.doFinal(data), "AES" );
        }
        catch(Exception e)
        {
            System.out.println ( "exception decrypting the aes key: " 
                                                   + e.getMessage() );
            return null;
        }

        return key;
    }
