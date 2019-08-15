    algorithm="DES";
    provider="GNU-CRYPTO";
    generator = KeyGenerator.getInstance(algorithm); <-works
    generator.init(randGenerator);
    SecretKey key = generator.generateKey();

    cipherEncryption = Cipher.getInstance(algorithm, provider); <- stops here
