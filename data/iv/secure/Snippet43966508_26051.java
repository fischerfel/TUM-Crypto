SecretKeyFactory factory = SecretKeyFactory
        .getInstance(KEY_DERIVATION_ALGORITHM);
    SecretKey tmp = factory.generateSecret(new PBEKeySpec(new String(dataKey).toCharArray(), SALT,
                                                          PBKDF_DEFAULT_ITERATIONS, 128));

    Key aesKey = new SecretKeySpec(tmp.getEncoded(), KEY_TYPE);
    Cipher cipher = Cipher.getInstance(CIPHER);
    cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(initializationVector));
    byte[] encrypted = cipher.doFinal(payload.getBytes());
