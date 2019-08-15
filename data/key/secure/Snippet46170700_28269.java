final static byte[] iv = new byte[16];//ADDED
final static int buffer = 102400;
final static String encryptionType = "AES/CFB8/NoPadding";//CHANGED TO DIFFERENT TYPE

static void encrypt(String password, File fileInput, File fileOutput) throws Exception {

    IvParameterSpec ivParams = new IvParameterSpec(iv);//ADDED

    FileInputStream fis = new FileInputStream(fileInput);
    FileOutputStream fos = new FileOutputStream(fileOutput);

    SecretKeySpec sks = new SecretKeySpec(password.getBytes("UTF-8"), encryptionType);

    Cipher cipher = Cipher.getInstance(encryptionType);
    //cipher.init(Cipher.ENCRYPT_MODE, sks);REPLACED
    cipher.init(Cipher.ENCRYPT_MODE, sks, ivParams);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int b;
    byte[] d = new byte[buffer];
    while ((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }

    cos.flush();
    cos.close();
    fis.close();
}

static void decrypt(String password, File fileInput, File fileOutput) throws Exception {

    IvParameterSpec ivParams = new IvParameterSpec(iv);//ADDED

    FileInputStream fis = new FileInputStream(fileInput);
    FileOutputStream fos = new FileOutputStream(fileOutput);

    SecretKeySpec sks = new SecretKeySpec(password.getBytes("UTF-8"), encryptionType);

    Cipher cipher = Cipher.getInstance(encryptionType);
    //cipher.init(Cipher.ENCRYPT_MODE, sks);REPLACED
    cipher.init(Cipher.DECRYPT_MODE, sks, ivParams);
    CipherInputStream cis = new CipherInputStream(fis, cipher);

    int b;
    byte[] d = new byte[buffer];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }

    fos.flush();
    fos.close();
    cis.close();
}
