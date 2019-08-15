/**
 * 初始化 AES Cipher
 * @param sKey
 * @param cipherMode
 * @return
 */
public static Cipher initAESCipher (String sKey, int cipherMode) throws Exception {
    //创建Key gen
    KeyGenerator keyGenerator = null;
    Cipher cipher = null;
        keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG" ,"Crypto");
        sr.setSeed(sKey.getBytes("UTF-8"));
        keyGenerator.init(128,sr);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] codeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
        cipher = Cipher.getInstance("AES");
        //初始化
        cipher.init(cipherMode, key);
    return cipher;
}

/**
 * 对文件进行AES加密
 * @param sourceFile
 * @param fileType
 * @param sKey
 * @return
 */
public static File encryptFile(File sourceFile,String fileType, String sKey) throws Exception {
    //新建临时加密文件
    File encrypfile = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;

        inputStream = new FileInputStream(sourceFile);
        encrypfile = new File(fileType);
        outputStream = new FileOutputStream(encrypfile);
        Cipher cipher = initAESCipher(sKey,Cipher.ENCRYPT_MODE);
        //以加密流写入文件
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = cipherInputStream.read(cache)) != -1) {
            outputStream.write(cache, 0, nRead);
            outputStream.flush();
        }
        cipherInputStream.close();
            inputStream.close();
            outputStream.close();
            sourceFile.delete();

    return encrypfile;
}
