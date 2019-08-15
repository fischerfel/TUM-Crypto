    try (DataOutputStream os = new DataOutputStream(new FileOutputStream(encryptedFile))) {
        os.writeInt(CURRENT_ENCRYPTION_FILE_VERSION);
        os.writeInt(CURRENT_RSA_KEY_VERSION);

        // Generate a new random symmetric key for the AES encryption cipher
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey aesKey = keyGenerator.generateKey();

        // Write the RSA encrypted AES key to the file
        os.write(doRsaTransformation(aesKey.getEncoded(), publicRsaKey, Cipher.ENCRYPT_MODE));

        // Generate an initialization vector (IV)
        SecureRandom sr = new SecureRandom();
        byte[] iv = new byte[16];
        sr.nextBytes(iv);

        // Write the RSA encrypted IV to the file
        os.write(doRsaTransformation(iv, publicRsaKey, Cipher.ENCRYPT_MODE));

        // Write the encrypted file
        doAesFileEncryption(fileToEncrypt, aesKey, iv, os);
    } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | IOException | InvalidAlgorithmParameterException ex) {
        throw new EncryptionException(ex);
    }

private void doAesFileEncryption(File fileToEncrypt, SecretKey aesKey, byte[] iv, OutputStream os) {
        Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));

        byte[] block = new byte[1024];
    int i;
    try (FileInputStream fis = new FileInputStream(fileToEncrypt)) {
        while ((i = fis.read(block)) != -1) {
            byte[] encryptedBlock = aesCipher.update(block, 0, i);
            if (encryptedBlock != null) {
                os.write(encryptedBlock);
            }
        }
        byte[] encryptedFinal = aesCipher.doFinal();
        if (encryptedFinal != null) {
            os.write(encryptedFinal);
        }
    }
}
