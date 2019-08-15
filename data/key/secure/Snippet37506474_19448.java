public void encryptFile(FileInfo fileInfo, File inFile, File outFile)
        throws Exception {

    //aesCipher = Cipher.getInstance("AES");
    aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    aeskeySpec = new SecretKeySpec(aesKey, "AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
    aesIV = aesCipher.getIV();

    FileInputStream is = new FileInputStream(inFile);
    CipherOutputStream os = new CipherOutputStream(new FileOutputStream(outFile),
            aesCipher);

    ObjectOutputStream objStream = new ObjectOutputStream(os);

    // File info class object is written
    objStream.writeObject(fileInfo);

    copy(is, os);

    is.close();
    os.close();
}

public void decryptFile(File inFile, File outFile)
        throws Exception {

    //aesCipher = Cipher.getInstance("AES");
    aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    aeskeySpec = new SecretKeySpec(aesKey, "AES");
    aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec, new IvParameterSpec(aesIV));

    CipherInputStream is = new CipherInputStream(new FileInputStream(inFile),
            aesCipher);
    FileOutputStream os = new FileOutputStream(outFile);

    MutableObjectClass objStream = new MutableObjectClass(is, FileInfo.class);
    FileInfo fileInfo = (FileInfo) objStream.readObject();

    //Refer file info object

    copy(is, os);

    is.close();
    os.close();
}
