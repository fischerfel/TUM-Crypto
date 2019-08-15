static void encrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    // Here you read the cleartext.
    FileInputStream fis = new FileInputStream("SampleFolder/yourfilename");
    // This stream write the encrypted text. This stream will be wrapped by another stream.
    FileOutputStream fos = new FileOutputStream("SampleFolder/yourencryptedfilename");

    // Length is 16 byte
    // Careful when taking user input!!! https://stackoverflow.com/a/3452620/1188357
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
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
