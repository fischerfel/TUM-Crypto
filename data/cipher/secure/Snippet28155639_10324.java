        // generate symmetric key
        kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, SecureRandom.getInstance("SHA1PRNG"));
        skey = kgen.generateKey();

        // create Cipher
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);

        // decode private key
        privk = KeyFactory.getInstance("RSA").generatePrivate(
            new PKCS8EncodedKeySpec(X)
        );

        // wrap symmetric key
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, privk);
        skey_buffer = cipher.wrap(skey);  
