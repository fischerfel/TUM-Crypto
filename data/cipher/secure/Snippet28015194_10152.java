 static void encrypt(String filename) throws IOException, NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException {

    // Here you read the cleartext.
    File extStore = Environment.getExternalStorageDirectory();
    startTime = System.currentTimeMillis();
    Log.i("Encryption Started",extStore + "/5mbtest/"+filename);
    FileInputStream fis = new FileInputStream(extStore + "/5mbtest/"+filename);
    // This stream write the encrypted text. This stream will be wrapped by
    // another stream.



    FileOutputStream fos = new FileOutputStream(extStore + "/5mbtest/"+filename+".aes", false);

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
            "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
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
    stopTime = System.currentTimeMillis();
    Log.i("Encryption Ended",extStore + "/5mbtest/"+filename+".aes");
    Log.i("Time Elapsed", ((stopTime - startTime)/1000.0)+"");
}

static void decrypt(String filename) throws IOException, NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException {
    File extStore = Environment.getExternalStorageDirectory();
    Log.i("Decryption Started",extStore + "/5mbtest/"+filename+".aes");
    FileInputStream fis = new FileInputStream(extStore + "/5mbtest/"+filename+".aes");

    FileOutputStream fos = new FileOutputStream(extStore + "/5mbtest/"+"decrypted"+filename,false);
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
            "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    startTime = System.currentTimeMillis();
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }

    stopTime = System.currentTimeMillis();

    Log.i("Decryption Ended",extStore + "/5mbtest/"+"decrypted"+filename);
    Log.i("Time Elapsed", ((stopTime - startTime)/1000.0)+"");

    fos.flush();
    fos.close();
    cis.close();
}
