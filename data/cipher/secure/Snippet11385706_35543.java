        BigInteger modulus = new BigInteger("blah");
        BigInteger exponent = new BigInteger("blah");

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

        KeyFactory encryptfact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = encryptfact.generatePublic(keySpec);

        String dataToEncrypt = "Hello World";

        /**
         * Encrypt data
         */
        Cipher encrypt = Cipher.getInstance("RSA");
        encrypt.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = encrypt.doFinal(dataToEncrypt.getBytes());

        System.out.println("cipherData: " + new String(cipherData));

        /**
         * Decrypt data
         */
        BigInteger privatemodulus = new BigInteger("blah");
        BigInteger privateexponent = new BigInteger("blah");

        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(privatemodulus, privateexponent);

        PrivateKey privateKey = encryptfact.generatePrivate(privateKeySpec);

        Cipher decrypt = Cipher.getInstance("RSA");
        decrypt.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decData = decrypt.doFinal(cipherData);

        System.out.println(new String(decData));
