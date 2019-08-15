byte[] keyByte = DatatypeConverter.parseHexBinary("007e151628aed2a6abf7158809cf4f3c");
Cipher aesCipher = Cipher.getInstance(transformation);
byte[] buf = new byte[1024];

for(int i = 0; i < 256; i++){
    keyByte[0] = (byte)i;

    FileInputStream inFileStream = new FileInputStream(path);
    File outFile = new File("AESencrypt_view" + String.valueOf(i)+".jpg");
    FileOutputStream outFileStream = new FileOutputStream(outFile);

    SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
    aesCipher.init(Cipher.DECRYPT_MODE, keySpec);

    int read;
    while((read = inFileStream.read(buf)) != -1){
        outFileStream.write(aesCipher.update(buf, 0, read));
    }
    inFileStream.close();

    try {
        outFileStream.write(aesCipher.doFinal());
        outFileStream.close();
    }
    catch(BadPaddingException e) {
        // obviously a wrong key or broken ciphertext
        outFileStream.close();
        outFile.delete();
    }
}
