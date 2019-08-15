public static byte[] encrypt(byte[] text, PublicKey key) throws Exception
    {
        byte[] cipherText = null;
        //
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
        return cipherText;
    }
