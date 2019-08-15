/*
     * This method will do the AES-256 encryption.
     */
    private byte[] encrypt(char[] raw, String cardno) {
     //  This raw is some unique key like password.      
        SecretKeyFactory factory = null;
        SecretKey tmp = null;
        Cipher cipher = null;
        byte[] ciphertext = null;
        AlgorithmParameters params = null;
        try {
            factory = SecretKeyFactory.getInstance("PBEWITHSHA-256AND256BITAES-CBC-BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeySpec spec = new PBEKeySpec(raw, mSalt, 1024, 256);

        try {
            if (factory != null)
                tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (tmp != null)
            mSecret = new SecretKeySpec(tmp.getEncoded(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            if (cipher != null)
                cipher.init(Cipher.ENCRYPT_MODE, mSecret);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (cipher != null)
            params = cipher.getParameters();
        try {

            mIV = params.getParameterSpec(IvParameterSpec.class).getIV();

        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        try {
            ciphertext = cipher.doFinal(cardno.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ciphertext;

    }





/*
     * This will decrypt the encrypted data based on provided key
     */
    private byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        //This raw is initialization vector generated while encrypting 
        Cipher cipher = null;
        byte[] decrypted = null;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, mSecret, new IvParameterSpec(raw));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        try {
            decrypted = cipher.doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        return decrypted;
    }
