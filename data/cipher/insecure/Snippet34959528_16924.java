try {
    encrypt(filePath);
} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
    e.printStackTrace();
}

public void encrypt(String image) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }; //Choose a key wisely
    FileInputStream fis = new FileInputStream(image);
    FileOutputStream fos = new FileOutputStream(image);

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    // Wrap the output stream
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    // Write bytes
    int b;
    byte[] d = new byte[8];
    while((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();
}
