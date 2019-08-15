public static void decrypt(FileInputStream fis,FileOutputStream fos ) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        // FileInputStream fis = new FileInputStream("data/encrypted");
        // FileOutputStream fos = new FileOutputStream("data/decrypted");
        String password ="passwordProtectd";
        byte[] inputByte = password.getBytes("UTF-8");
        SecretKeySpec sks = new SecretKeySpec(inputByte, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }
