public static String encrypt(String plainFile, String encryptedFile) throws    IOException, NoSuchAlgorithmException,
NoSuchPaddingException, InvalidKeyException {
    // Here you read the cleartext.
    File extStore = Environment.getExternalStorageDirectory();
    FileInputStream fis = new FileInputStream(plainFile);
    // This stream write the encrypted text. This stream will be wrapped by
    // another stream.
    FileOutputStream fos = new FileOutputStream(encryptedFile);

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    // Wrap the output stream
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    // Write bytes
    int b;
    byte[] d = new byte[8];
    while ((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();

    return encryptedFile;
}   

static String decrypt(String plainFile, String encryptedFile) throws IOException, NoSuchAlgorithmException,
NoSuchPaddingException, InvalidKeyException {

    File encFile=new File(encryptedFile);
    FileInputStream fis = new FileInputStream(encFile);

    FileOutputStream fos = new FileOutputStream(plainFile);
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
              "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();

    return plainFile;
}    
