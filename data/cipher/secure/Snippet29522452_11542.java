private static String password;
private static String salt;
private static int pswdIterations = 65536  ;
private static int keySize = 256;
private byte[] ivBytes;
public void encryptToFile(byte[] bytes, File out) throws Exception {  
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

    CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out, true), cipher);
    os.write(bytes);
    os.close();

}

public byte[] decryptToFile(File in) throws Exception {  
    byte[] saltBytes = salt.getBytes("UTF-8");

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

    // Decrypt the message
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

    FileInputStream is = new FileInputStream(in);
    byte[] encBytes = new byte[(int) in.length()];
    is.read(encBytes);
is.close();    

    byte[] decryptedBytes = null;
    try {
        decryptedBytes = cipher.doFinal(encBytes);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }

    return decryptedBytes;

}
