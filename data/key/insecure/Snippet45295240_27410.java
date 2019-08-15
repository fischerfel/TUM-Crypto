public static void encrypt(InputStream fis,FileOutputStream fos ) throws IOException, NoSuchAlgorithmException
            , NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        //FileInputStream fis = new FileInputStream("data/cleartext");
        // This stream write the encrypted text. This stream will be wrapped by another stream.
        // FileOutputStream fos = new FileOutputStream("data/encrypted");

        String password ="passwordProtectd";
        // Length is 16 byte
        byte[] inputByte = password.getBytes("UTF-8");
        SecretKeySpec sks = new SecretKeySpec(inputByte, "AES");
        // SecretKeySpec sks = new SecretKeySpec(password.getBytes(), "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }
