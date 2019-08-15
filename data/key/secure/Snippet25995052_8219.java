        static byte a[] = { ..... };

        SecretKeySpec key;

        if (key == null)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(a);
            bos.write(abyte1);
            key = new SecretKeySpec(bos.toByteArray(), "AES");
            bos.close();
        }
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, key);
        CipherInputStream cipherinputstream = new CipherInputStream(inputstream, cipher);
