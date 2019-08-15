    private static void decrypt()throws NoSuchAlgorithmException,           NoSuchPaddingException, InvalidKeyException, IOException{

    byte[] b = key.getBytes();


             //System.out.println(Arrays.toString(data));
    //CipherInputStream is = new CipherInputStream(new FileInputStream("AESencrypt_1.jpg"), aesCipher);
    //CipherOutputStream os = new CipherOutputStream("decPic.jpg");
    FileInputStream file = new FileInputStream(path);
    FileOutputStream out = new FileOutputStream("Decrypted.jpg");
    Cipher aesCipher = Cipher.getInstance(transformation);

       SecretKeySpec key1 = new SecretKeySpec(b,"AES");
               aesCipher.init(Cipher.DECRYPT_MODE, key1);
    CipherOutputStream outSt = new CipherOutputStream(out,aesCipher);
    byte[] buf = new byte[1024];
    int read;
    while((read=file.read(buf))!=-1){
        outSt.write(buf, 0, read);

    }

    file.close();
    out.flush();
    outSt.flush();
