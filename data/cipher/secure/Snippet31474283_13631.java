public void decrypt(String inputFile){
    FileInputStream fis = new FileInputStream(inputFile);
    // Save file: filename.dec
    FileOutputStream fos = new FileOutputStream(inputFile.substring(0,
            inputFile.length() - 4) + ".dec");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    // Read from file encrypted  ---> .dec 
    int readByte;
    byte[] buffer = new byte[1024];
    while ((readByte = fis.read(buffer)) != -1) {
        fos.write(cipher.doFinal(buffer), 0, readByte);
    }
    fos.flush();
    fos.close();
    fis.close();
}
