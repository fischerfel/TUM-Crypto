        // get key from Android kyestore
        SecretKey secretKey = (SecretKey)mKeyStore.getKey(KEY_ALIAS, null);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        IvParameterSpec ivParams = cipher.getParameters().getParameterSpec(IvParameterSpec.class);

        //
        // Encrypt
        //
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
        cipherOutputStream.write(plainString.getBytes("UTF-8"));
        cipherOutputStream.close();

        String encryptedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        //
        // Decrypt
        //
        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(encryptedString, Base64.DEFAULT));
        CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, decryptCipher);
        outputStream = new ByteArrayOutputStream();

        int b;
        while ((b = cipherInputStream.read()) != -1) {
            outputStream.write(b);
        }
        outputStream.close();

        String decryptedString =  outputStream.toString("UTF-8");

        //
        // results. decrypted string is part of source string and short.
        //
        int sourceStringLength = plainString.length(); // is 183244;
        int decryptedStringLength = decryptedString.length()); // is UNEXPECTED 68553;
