public void encrypt(String inputFile){
    FileInputStream fis = new FileInputStream(inputFile);
    // Save file: inputFile.enc
    FileOutputStream fos = new FileOutputStream(inputFile + ".enc");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);

    AlgorithmParameters params = cipher.getParameters();
    // Gen Initialization Vector
    iv = (byte[]) ((IvParameterSpec) params
            .getParameterSpec(IvParameterSpec.class)).getIV();
    // read from file (plaint text)  -----> save with .enc
    int readByte;
    byte[] buffer = new byte[1024];
    while ((readByte = fis.read(buffer)) != -1) {
        fos.write(cipher.doFinal(buffer), 0, readByte);
    }
    fis.close();
    fos.flush();
    fos.close();
}
