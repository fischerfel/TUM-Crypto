    Cipher cipher;
    Key publicKey = null;

    try 
    {
        cipher = Cipher.getInstance("RSA", "BC");




    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
        String msg = "failed to create output stream";
        LOGGER.error( msg, e );
        throw new RuntimeException( msg, e );
    }

    try {
        publicKey = getPublicKey(publicKeyPath);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return ( new CipherOutputStream(outputStream, cipher));
