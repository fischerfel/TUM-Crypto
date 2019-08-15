static void Decrypt() throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Encrypted");
        FileInputStream fis = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;

        bytes = IOUtils.toByteArray(fis);

        byte[] N = new byte[(int) length - offset];

        int g, s = 0;

        for (g = offset; g < length; g++) {
            N[s++] = bytes[g];
        }

        FileOutputStream fos = new FileOutputStream(Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/Decrypted");

        SecretKeySpec sks = new SecretKeySpec(
                "12345678901234567890123456789012".getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);

        byte[] b = cipher.update(N);


        int j = 0;
        while (j < b.length) {

            fos.write(b[j]);
            j++;
        }

        fos.flush();
        fos.close();

    }
