public void encrypt() {
    doCrypto(Cipher.ENCRYPT_MODE, KEY);
}

public void decrypt() {
    doCrypto(Cipher.DECRYPT_MODE, KEY);
}

private void doCrypto(int cipherMode, String key) {
    try {
        Key secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(this);
        FileOutputStream fileOutputStream = new FileOutputStream(this);

        int read;

        CipherInputStream cis = new CipherInputStream(inputStream, cipher);
        CipherOutputStream cos = new CipherOutputStream(fileOutputStream, cipher);

        while ((read = cis.read()) != -1) {
            cos.write(read);
            cos.flush();
        }
        cos.close();
        cis.close();

        inputStream.close();
        fileOutputStream.close();

    } catch (NoSuchPaddingException | NoSuchAlgorithmException
            | InvalidKeyException | IOException ex) {
        throw new RuntimeException("Error encrypting/decrypting file", ex);
    }
}
