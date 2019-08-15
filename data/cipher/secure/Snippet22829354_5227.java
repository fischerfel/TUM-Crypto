try (FileInputStream fis = new FileInputStream(sourceFile)) {
    response = Utils.decryptEnv((byte[]) tempResponse.getObjContents().get(0), fsSecretKey, ivSpec);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

    do {
        byte[] buf = new byte[4096];

        int n = fis.read(buf); // can throw an IOException
        else if (n < 0) {
            System.out.println("Read error");
            fis.close();
            return false;
        }

        byte[] cipherBuf = cipher.doFinal(buf);

        // send through socket blah blah blah

    } while (fis.available() > 0);
