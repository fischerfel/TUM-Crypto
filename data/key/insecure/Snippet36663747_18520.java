static void encrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    // Here you read the cleartext.
    FileInputStream fis = new FileInputStream("data/cleartext");
    // This stream write the encrypted text. This stream will be wrapped by another stream.
    FileOutputStream fos = new FileOutputStream("data/encrypted");

    // Length is 16 byte
    // Careful when taking user input!!! http://stackoverflow.com/a/3452620/1188357
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



static void decrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream("data/encrypted");

    FileOutputStream fos = new FileOutputStream("data/decrypted");
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
}


//put the last part in the method
static void generate() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
    FileInputStream fis;
    FileOutputStream fos;
    CipherOutputStream cos;
    // File you are reading from
    fis = new FileInputStream("/tmp/a.txt");
    // File output
    fos = new FileOutputStream("/tmp/b.txt");

    // Here the file is encrypted. The cipher1 has to be created.
    // Key Length should be 128, 192 or 256 bit => i.e. 16 byte
    SecretKeySpec skeySpec = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES"); 
    Cipher cipher1 = Cipher.getInstance("AES");  
    cipher1.init(Cipher.ENCRYPT_MODE, skeySpec);
    cos = new CipherOutputStream(fos, cipher1);
    // Here you read from the file in fis and write to cos.
    byte[] b = new byte[8];
    int i = fis.read(b);
    while (i != -1) {
        cos.write(b, 0, i);
        i = fis.read(b);
    }
    cos.flush();
}
