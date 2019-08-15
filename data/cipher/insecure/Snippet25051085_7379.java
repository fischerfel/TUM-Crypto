        FileInputStream in = new FileInputStream("YOUR DIRECTORY");
        Cipher decryptionCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, createKey(password));

        CipherInputStream ciphIn = new CipherInputtStream(in, decryptionCipher);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8];
        int i = in.read(buffer);
        while (i != -1)
        {
            //just like a file copy
            out.write(buffer, 0, i)
            i = in.read(buffer);
        }

        /*
         * YOUR NATIVE DATA UNENCRYPTED
         */
        byte[] nativeFileData = out.toByteArray();

        ciphIn.close();
        in.close();
