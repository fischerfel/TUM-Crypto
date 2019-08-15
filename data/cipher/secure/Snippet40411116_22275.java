void encrypt() throws Exception{
    char[] password = passwordText.getText().toCharArray();
    byte[] salt = new byte[8];

    /* Creating and saving salt */
    salt = saveSalt(salt);

    /* Securing password */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    if (choosedFile != null) {
        /* Choosing algorithm for decryption */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        /* Getting plain file */
        CipherInputStream fis = new CipherInputStream(new  FileInputStream(choosedFile), cipher);
        CipherOutputStream fos = new CipherOutputStream(new FileOutputStream(choosedFile+".encrypted"), cipher);

        /* Encrypting and Measuring */
        long startTime = System.currentTimeMillis();
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] rawText = new byte[128];
        int count;
        while((count = fis.read(rawText)) > 0) {
            System.out.println(count);
            byte[] encryptedText = cipher.doFinal(rawText);
            fos.write(encryptedText, 0, count);
        }
        long stopTime = System.currentTimeMillis();

        fis.close();
        fos.close();

        /* Creating initialization vector and storing*/
        byte[] iVector = cipher.getIV();
        saveIVector(iVector);

        text.setText(text.getText() + "File was encrypted in " + (stopTime - startTime) + "ms.\n");
    }

}
