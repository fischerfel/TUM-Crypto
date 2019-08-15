public void testPBEWithSHA1AndAES() throws Exception {
        String password = "test";
        String message = "Hello World!";

        byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
        byte[] iv = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99,
                (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

        int count = 1024;
        // int keyLength = 256;
        int keyLength = 128;

        String cipherAlgorithm = "AES/CBC/PKCS5Padding";
        String secretKeyAlgorithm = "PBKDF2WithHmacSHA1";
        SecretKeyFactory keyFac = SecretKeyFactory
                .getInstance(secretKeyAlgorithm);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt,
                count, keyLength);
        SecretKey tmp = keyFac.generateSecret(pbeKeySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        Cipher ecipher = Cipher.getInstance(cipherAlgorithm);
        ecipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));

        // decrypt
        keyFac = SecretKeyFactory.getInstance(secretKeyAlgorithm);
        pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, count,
                keyLength);
        tmp = keyFac.generateSecret(pbeKeySpec);
        secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        // AlgorithmParameters params = ecipher.getParameters();
        // byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        Cipher dcipher = Cipher.getInstance(cipherAlgorithm);
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

        byte[] encrypted = ecipher.doFinal(message.getBytes());
        byte[] decrypted = dcipher.doFinal(encrypted);
        assertEquals(message, new String(decrypted));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CipherOutputStream cipherOut = new CipherOutputStream(out, ecipher);
        cipherOut.write(message.getBytes());
        StreamUtils.closeQuietly(cipherOut);
        byte[] enc = out.toByteArray();

        ByteArrayInputStream in = new ByteArrayInputStream(enc);
        CipherInputStream cipherIn = new CipherInputStream(in, dcipher);
        ByteArrayOutputStream dec = new ByteArrayOutputStream();
        StreamUtils.copy(cipherIn, dec);
        assertEquals(message, new String(dec.toByteArray()));
    }
