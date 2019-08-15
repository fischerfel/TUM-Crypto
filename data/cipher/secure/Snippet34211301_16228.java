public String decryptUsingRSA(String encryptedString) {

    byte[] encryptedBytes = null;
    String modulusElem = "......";
    String dElem = ".....";
    String result = "";

    try {
        /*
         * ================================================================================================
         *      DECODING FROM BASE64
         * ================================================================================================
         */
        logger.info("decryptUsingRSA : DECODING FROM BASE64");
        byte[] modBytes = Base64.decode(modulusElem);
        byte[] dBytes = Base64.decode(dElem);
        encryptedBytes = Base64.decode(encryptedString);

        /*
         * ================================================================================================
         *      CONVERTING TO BIGINTEGER
         * ================================================================================================
         */
        logger.info("decryptUsingRSA : CONVERTING TO BIGINTEGER");
        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger d = new BigInteger(1, dBytes);

        /*
         * ================================================================================================
         *      INSTANTIATING KEYFACTORY AND CIPHER
         * ================================================================================================
         */
        logger.info("decryptUsingRSA : INSTANTIATING KEYFACTORY AND CIPHER");
        KeyFactory factory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");

        /*
         * ================================================================================================
         *      GENERATING PRIVATE KEY OBJECT
         * ================================================================================================
         */
        logger.info("decryptUsingRSA : GENERATING PRIVATE KEY OBJECT");
        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        PrivateKey privKey = factory.generatePrivate(privSpec);

        /*
         * ================================================================================================
         *      DECRYPTING
         * ================================================================================================
         */
        logger.info("decryptUsingRSA : DECRYPTING");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        result = new String(decrypted);
        logger.info("decryptUsingRSA : result = " + result);
    } catch (Exception ex) {
        logger.info("decryptUsingRSA : Exception occurred ");
        ex.printStackTrace();
    }

    return result;
}

public String encryptUsingRSA(String decryptedString) {

    decryptedString = "Encrypted using RSA";
    String modulusElem = "....";
    String dElem = "....";
    String result = "";

    try {
        /*
         * ================================================================================================
         *      DECODING FROM BASE64
         * ================================================================================================
         */
        logger.info("encryptUsingRSA : Step 1/5 - DECODING FROM BASE64");   
        byte[] modBytes = Base64.decode(modulusElem);
        byte[] dBytes = Base64.decode(dElem);

        /*
         * ================================================================================================
         *      CONVERTING TO BIGINTEGER
         * ================================================================================================
         */
        logger.info("encryptUsingRSA : Step 2/5 - CONVERTING TO BIGINTEGER");   
        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger d = new BigInteger(1, dBytes);

        /*
         * ================================================================================================
         *      INSTANTIATING KEYFACTORY AND CIPHER
         * ================================================================================================
         */
        logger.info("encryptUsingRSA : Step 3/5 - INSTANTIATING KEYFACTORY AND CIPHER");    
        KeyFactory factory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // tried "RSA", "RSA/NONE/PKCS1Padding" etc but same error

        /*
         * ================================================================================================
         *      GENERATING PRIVATE KEY OBJECT
         * ================================================================================================
         */
        logger.info("encryptUsingRSA : Step 4/5 - GENERATING PRIVATE KEY OBJECT");
        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        PrivateKey privKey = factory.generatePrivate(privSpec);
        //logger.info("encryptUsingRSA : Step 4/5 - privKey = " + Base64.encode(privKey.getEncoded()));

        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modules, d);
        PublicKey pubKey = factory.generatePublic(publicSpec);
        //logger.info("encryptUsingRSA : Step 4/5 - pubKey = " + Base64.encode(pubKey.getEncoded()));

        /*
         * ================================================================================================
         *      DECRYPTING
         * ================================================================================================
         */
        logger.info("encryptUsingRSA : Step 5/5 - DECRYPTING");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] decryptedByteArray = decryptedString.getBytes();
        byte[] encryptedbyteArray = cipher.doFinal(decryptedByteArray);

        decryptUsingRSA(Base64.encode(encryptedbyteArray));
    } catch (Exception ex) {
        logger.info("encryptUsingRSA : ERROR encrypting input: " + ex.getMessage());
        ex.printStackTrace();
    }

    return result;
}
