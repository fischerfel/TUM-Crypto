private static String password;
private static String salt;
private static int pswdIterations = 65536  ;
private static int keySize = 256;
private byte[] ivBytes;

public void encryptOfFile(byte[] bytes, File out) throws Exception {  

    byte[] saltBytes = salt.getBytes("UTF-8");

    System.out.println("Salt bfre:" +salt);

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            saltBytes,
            pswdIterations,
            keySize
            );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    //encrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

    //First copy the IVBytes at the beginning  of the file
    System.out.println("IvBytes Bfore: "  + DatatypeConverter.printHexBinary(ivBytes));
    FileOutputStream os = new FileOutputStream(out, true);
    os.write(ivBytes);

    CipherOutputStream cos = new CipherOutputStream(os, cipher);
    cos.write(bytes);

    os.close();
    cos.close();

}

public byte[] decryptToBytes(File in) throws Exception {  
    byte[] saltBytes = salt.getBytes("UTF-8");
    System.out.println("Salt afetr:" +salt);

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            saltBytes,
            pswdIterations,
            keySize
            );

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");


    //Get IVBytes of the first 16 bytes of the file
    System.out.println("File Size: "  + in.length());

    FileInputStream is = new FileInputStream(in);
    byte [] ivBytesRecovered = new byte [16];
    if (is.read(ivBytesRecovered) != ivBytesRecovered.length) {
        //is.close();
        throw new IllegalStateException("Too short file");
    }
    System.out.println("IvBytes After: "  + DatatypeConverter.printHexBinary(ivBytesRecovered));

    // Decrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytesRecovered));        

    byte[] encBytes = new byte[(int) in.length()-16];
    is.read(encBytes);

    byte[] decryptedBytes = null;
    try {
        decryptedBytes = cipher.doFinal(encBytes);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }

    is.close();

    return decryptedBytes;

}
