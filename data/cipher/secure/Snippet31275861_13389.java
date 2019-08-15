private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

public static void decrypt(String inputFile, String outputFile, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        FileInputStream fis = new FileInputStream(inputFile);

        FileOutputStream fos = new FileOutputStream(outputFile);

        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec sks = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, sks, iv);
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        int b;
        byte[] d = new byte[1024];
        while((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }
