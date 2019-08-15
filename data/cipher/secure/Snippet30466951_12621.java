private void encrypt(String password) {
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec ks = new PBEKeySpec(
            password.toCharArray(),
            "salt".getBytes(),
            1024,
            256
    );
    SecretKey s = f.generateSecret(ks);
    java.security.Key key = new SecretKeySpec(s.getEncoded(), "AES/CBC/PKCS5Padding");

    InputStream input = new FileInputStream("PATH_TO_IMAGE");   
    BufferedInputStream bis = new BufferedInputStream(input);

    FileOutputStream fos = new FileOutputStream("PATH_TO_ENCRYPTED_FILE");
    BufferedOutputStream bos = new BufferedOutputStream(fos);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] buff = new byte[32 * 1024];

    CipherOutputStream output = new CipherOutputStream(bos, cipher);
    int len;
    while ((len = bis.read(buff)) > 0) {
        output.write(buff, 0, len);
    }
    output.flush();

    // closing streams ...
}
