public myDecryptMethod(byte[] sessionKey, FileInputStream encryptedFileStream) throws Exception{
....
    SecretKeySpec symmKeySpec = new SecretKeySpec(sessionKey, "AES/CBC/PKCS5Padding");
    Cipher symmCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    IvParameterSpec ivParameterSpec = new IvParameterSpec("0000000000000000".getBytes());
    symmCipher.init(Cipher.DECRYPT_MODE, symmKeySpec, ivParameterSpec);

    CipherInputStream cis = new CipherInputStream(encryptedFileStream, symmCipher);

    byte[] inputByteArray = new byte[10240];
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    while (true) {
        int length = cis.read(inputByteArray);
        if (length < 0)
            break;
        outStream.write(inputByteArray, 0, length);
    }

    outStream.close();
    byte[] data = outStream.toByteArray();
    InputStream inStream = new ByteArrayInputStream(data);

    cis.close();

    return inStream;
}
