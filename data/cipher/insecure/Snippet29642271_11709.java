Encrypt(BufferedInputStream is, File destfile, String passcode) {
        bis = is;
        try {
            fos = new FileOutputStream(destfile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dest = new BufferedOutputStream(fos, 1024);
        this.passcode = passcode;
    }

    static void encrypt() throws IOException, NoSuchAlgorithmException,
    NoSuchPaddingException, InvalidKeyException {

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec(passcode.getBytes(), "AES");

        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[1024];
        while ((b = bis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        bis.close();
    }
