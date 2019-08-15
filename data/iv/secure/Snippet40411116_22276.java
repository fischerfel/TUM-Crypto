    void decrypt() throws Exception {
    /* Getting salt */
    byte[] salt = getSalt();
    /* Getting initialization vector */
    byte[] iVector = getIVector();
    /* Getting user password */
    char[] password = passwordText.getText().toCharArray();


    /* Securing password */
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    if (choosedFile != null) {

        /* Choosing algorithm for decryption */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        /* Getting ciphered file */


        CipherInputStream fis = new CipherInputStream(new  FileInputStream(choosedFile), cipher);
        CipherOutputStream fos = new CipherOutputStream(new FileOutputStream(choosedFile+".decrypted"), cipher);

        /* Decrypting and Measuring */
        long startTime = System.currentTimeMillis();
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iVector));
        byte[] rawText = new byte[128];
        int count;
        while((count = fis.read(rawText)) > 0) {
                byte[] encryptedText = cipher.doFinal(rawText);
                fos.write(encryptedText, 0, count);
            }

        long stopTime = System.currentTimeMillis();

        fis.close();
        fos.close();
