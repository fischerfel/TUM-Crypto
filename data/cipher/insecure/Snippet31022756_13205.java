public static void encryptAndClose(FileInputStream fis, FileOutputStream fos) 
        throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

    // Length is 16 byte
    SecretKeySpec sks = new SecretKeySpec("1234567890123456".getBytes(), "AES");
    // Create cipher
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);      

    // Wrap the output stream for encoding
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);       

    //wrap output with buffer stream
    BufferedOutputStream bos = new BufferedOutputStream(cos);     

    //wrap input with buffer stream
    BufferedInputStream bis = new BufferedInputStream(fis); 

    // Write bytes
    int b;
    byte[] d = new byte[8];
    while((b = bis.read(d)) != -1) {
        bos.write(d, 0, b);
    }
    // Flush and close streams.
    bos.flush();
    bos.close();
    bis.close();
}


public static void decryptAndClose(FileInputStream fis, FileOutputStream fos) 
        throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

    SecretKeySpec sks = new SecretKeySpec("1234567890123456".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);

    CipherInputStream cis = new CipherInputStream(fis, cipher);

    //wrap input with buffer stream
    BufferedInputStream bis = new BufferedInputStream(cis); 

    //wrap output with buffer stream
    BufferedOutputStream bos = new BufferedOutputStream(fos);       

    int b;
    byte[] d = new byte[8];
    while((b = bis.read(d)) != -1) {
        bos.write(d, 0, b);
    }
    bos.flush();
    bos.close();
    bis.close();
}
