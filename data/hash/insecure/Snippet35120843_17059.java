public static void encrypt(String path, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(path);
    FileOutputStream fos = new FileOutputStream(path.concat(".scrypt"));
    byte[] key = md5(password).getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key,16);
    SecretKeySpec sks = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    int b;
    byte[] d = new byte[8];
    while((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }
    cos.flush();
    cos.close();
    fis.close();
}

public static void decrypt(String path, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(path);
    FileOutputStream fos = new FileOutputStream(path.replace(".scrypt", ""));
    byte[] key = md5(password).getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key,16);
    SecretKeySpec sks = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }
    fos.flush();
    fos.close();
    cis.close();
}
