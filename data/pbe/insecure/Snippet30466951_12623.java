private void decrypt(String password) {
    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec ks = new PBEKeySpec(
            password.toCharArray(),
            "salt".getBytes(),
            1024,
            256
    );
    SecretKey s = f.generateSecret(ks);
    java.security.Key key = new SecretKeySpec(s.getEncoded(), "AES/CBC/PKCS5Padding");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key);

    InputStream inputStream = new FileInputStream("PATH_TO_ENCRYPTED_FILE");
    CipherInputStream input = new CipherInputStream(inputStream, cipher);
    byte[] data = inputStreamToByteArray(input);

    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

    // closing streams ...
}

public static byte[] inputStreamToByteArray(CipherInputStream inputStream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int len;
    while ((len = inputStream.read(buffer)) > -1) {
        baos.write(buffer, 0, len);
    }
    baos.flush();

    try {
        return baos.toByteArray();
    } finally {
        baos.close();
    }
}
