        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128); // 192 and 256 bits may not be available

    SecretKey secretKey = keyGenerator.generateKey();

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


    // By initializing the cipher in CBC mode, an "initialization vector" has been randomly
    // generated. This initialization vector will be necessary to decrypt the encrypted data.
    // It is safe to store the initialization vector in plain text for later use. You can obtain
    // it's bytes by calling iv.getIV().
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    IvParameterSpec iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
        //      IvParameterSpec iv = new IvParameterSpec(IV); //used for the hardcoded one

        byte[] encryptedData = cipher.doFinal(data);
