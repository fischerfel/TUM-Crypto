    byte password[] = (WHAT YOUR WANT. STRING, NUMBER, etc.).getBytes();
    DESKeySpec desKeySpec;
    try {

        desKeySpec = new DESKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(desKeySpec);

        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, key);

        // Create stream
        FileOutputStream fos = new FileOutputStream("Your file here");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
    }
