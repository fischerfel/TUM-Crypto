        // read the key
    FileInputStream fis = new FileInputStream(keyFile);
    byte[] keyBytes = new byte[fis.available()];
    fis.read(keyBytes);
    SecretKeySpec spec = new SecretKeySpec(keyBytes, "DES");

    // encrypt
    Cipher encCipher = Cipher.getInstance("DES");
    encCipher.init(Cipher.ENCRYPT_MODE, spec);

    CipherInputStream cipherIn = new CipherInputStream(new FileInputStream(zipFile), encCipher);
    FileChannel out = new FileOutputStream(encZipFile).getChannel();
    out.transferFrom(Channels.newChannel(cipherIn), 0, Long.MAX_VALUE);

    // decrypt
    Cipher decCipher = Cipher.getInstance("DES");
    decCipher.init(Cipher.DECRYPT_MODE, spec);

    cipherIn = new CipherInputStream(new FileInputStream(encZipFile), decCipher);
    out = new FileOutputStream(decZipFile).getChannel();
    out.transferFrom(Channels.newChannel(cipherIn), 0, Long.MAX_VALUE);
