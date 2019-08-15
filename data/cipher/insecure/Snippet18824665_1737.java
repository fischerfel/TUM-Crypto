      static void encrypt(String inputPath, String outputPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException 
      {
     // Here you read the cleartext.
    FileInputStream fis = new FileInputStream(inputPath);
    // This stream write the encrypted text. This stream will be wrapped by another stream.
    FileOutputStream fos = new FileOutputStream(outputPath);

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, sks);
    // Wrap the output stream
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    // Write bytes
    int b;
    int count = 0;
    byte[] d = new byte[1024];

    while((b = fis.read(d)) != -1) {
        if(count <= 1024){
            count += b;
            cos.write(d, 0, b);
        }else{

            cos.write(d, 0, b);

        }
       // cos.write(d, 0, b);
    }
    // Flush and close streams.
    cos.flush();
    cos.close();
    fis.close();
}

static byte[] decrypt(String inputPath) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(inputPath);

   // FileOutputStream fos = new FileOutputStream(outputPath);
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);

    int b;
    byte[] d = new byte[1024];
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int count =0;
    while((b = cis.read(d)) != -1) {
        if(count <= 1024){
            count += b;
            bos.write(d, 0, b);
        }else{

            bos.write(d, 0, b);

        }

    }

     byte[] completeBytes = bos.toByteArray();
    cis.close();
    return completeBytes;
}
