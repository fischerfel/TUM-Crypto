void encrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
InvalidKeyException {
    String myString = getOutputFile();
    File myFile = new File(myString);
    FileInputStream inputStream = new FileInputStream(myFile);
    File encodedfile = new File(path,"filename" + ".mp4");
    FileOutputStream outputStream = new FileOutputStream(encodedfile);
    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);
    int b;
    byte[] d = new byte[8];
    while((b = inputStream.read(d)) != -1){
        cos.write(d, 0, b);
    }
    cos.flush();
    cos.close();
    inputStream.close();
