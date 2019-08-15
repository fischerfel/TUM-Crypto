static void encrypt(String strInput , String strOutput) throws IOException,
    NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(strInput);
    FileOutputStream fos = new FileOutputStream(strOutput);

    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
            "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();
}
