    // file to be encrypted
    File file = new File("D:\\SynchData\\output.xls");
    FileInputStream inFile = new FileInputStream(file);

    FileOutputStream outFile = new FileOutputStream("D:\\SynchData\\output.dec");

    // password to encrypt the file
    String password = "MAKV2SPBNI99212";

    // password, iv and salt should be transferred to the other end
    // in a secure manner

    // salt is used for encoding
    // writing it to a file
    // salt should be transferred to the recipient securely
    // for decryption
    byte[] salt = new byte[16];
    /*SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(salt);*/
    FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
    saltOutFile.write(salt);
    saltOutFile.close();

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 1024, 128);
    SecretKey secretKey = factory.generateSecret(keySpec);
    SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();

    // iv adds randomness to the text and just makes the mechanism more
    // secure
    // used while initializing the cipher
    // file to store the iv
    FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    log.info("iv length: "+iv.length);
    ivOutFile.write(iv);
    ivOutFile.close();

    //file encryption
    byte[] input = new byte[64];
    int bytesRead;

    while ((bytesRead = inFile.read(input)) != -1) {
        byte[] output = cipher.update(input, 0, bytesRead);
        if (output != null)
            outFile.write(output);
    }

    byte[] output = cipher.doFinal();
    if (output != null)
        outFile.write(output);

    inFile.close();
    outFile.flush();
    outFile.close();

    log.info("File Encrypted.  exportEncryptFile");
