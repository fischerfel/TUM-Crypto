    // generate a key
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);  // To use 256 bit keys, you need the "unlimited strength" encryption policy files from Sun.
        //byte[] key = keygen.generateKey().getEncoded();
        byte key[] = {0x00, 0x01, 0x02, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0C, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");


        SecureRandom random = new SecureRandom();

        IvParameterSpec ivspec = new IvParameterSpec(key);

        // initialize the cipher for encrypt mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

        System.out.println();

        byte[] encrypted = cipher.doFinal(IOUtils.toByteArray(new FileInputStream(new File(fileName))));
