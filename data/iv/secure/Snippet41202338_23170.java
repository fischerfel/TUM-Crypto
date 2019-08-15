char[] password = passwordString.toCharArray();

    File file = new File(PRIVATE_RING_FILENAME);
    FileInputStream fin = new FileInputStream(file);


    Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");


    byte[] salt = new byte[SALT_BYTES];

    fin.read(salt);


    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");


    KeySpec keySpec = new PBEKeySpec(password, salt, ITERATION, AES_KEY_BITS);

    SecretKey = factory.generateSecret(keySpec);

    SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");


        byte[] ivN = new byte[AES_BYTES];
        fin.read(ivN, 0, AES_BYTES);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivN));

    CipherInputStream cis = new CipherInputStream(fin, cipher);
    ObjectInputStream objIn;
    PrivateKeyRing prvKeyRing = null;
    SealedObject sealedObject = null;
    objIn = new ObjectInputStream(cis);

    sealedObject = (SealedObject) objIn.readObject();
    prvKeyRing = (PrivateKeyRing) sealedObject.getObject(cipher);

        objIn.close();
        fin.close();
        cis.close();
