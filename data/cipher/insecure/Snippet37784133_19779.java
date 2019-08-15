public static String decrypt(byte[] text, PublicKey key) throws InvalidKeyException,
    BadPaddingException,
    IllegalBlockSizeException,
    NoSuchPaddingException,
    NoSuchAlgorithmException{
        byte[] dectyptedText = null;

        // get an RSA cipher object and print the provider
        String transformation = "RSA/ECB/PKCS1Padding";
        final Cipher cipher = Cipher.getInstance(transformation);

        // decrypt the text using the private key
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);



        return new String(dectyptedText);
    }
