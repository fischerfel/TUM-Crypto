char[] password = passwordString.toCharArray();

    SecureRandom random = new SecureRandom();
    byte salt[] = new byte[SALT_BYTES]; 
    random.nextBytes(salt);

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    KeySpec keySpec = new PBEKeySpec(password, salt, ITERATION, AES_KEY_BITS);

    SecretKey tmp = factory.generateSecret(keySpec);

    SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    FileOutputStream fout = null;
    ObjectOutputStream objOut = null;


        fout = new FileOutputStream(PRIVATE_RING_FILENAME);

        fout.write(salt);

        byte[] ivN = cipher.getIV();
        fout.write(ivN);

        CipherOutputStream cos = new CipherOutputStream(fout, cipher);
        objOut = new ObjectOutputStream(cos);

        PrivateKeyRing prvKeyRing = new PrivateKeyRing();
        SealedObject sealedObject = new SealedObject(prvKeyRing, cipher);
        objOut.writeObject(sealedObject);

        fout.close();
        objOut.close();
        cos.close();
