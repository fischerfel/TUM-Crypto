public static void main(String[] args) throws Exception {
    encrypt("samplePassword", new FileInputStream("file.txt"), new FileOutputStream("enc-file.txt"));
    decrypt("samplePassword", new FileInputStream("enc-file.txt"), new FileOutputStream("file-from-enc.txt"));
}

public static void encrypt(String password, InputStream is, OutputStream os) throws Exception {

    SecretKeySpec keySpec = new SecretKeySpec(password(password), "TripleDES");
    Cipher cipher = Cipher.getInstance("TripleDES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    byte[] buf = new byte[8096];
    os = new CipherOutputStream(os, cipher);
    int numRead = 0;
    while ((numRead = is.read(buf)) >= 0) {
        os.write(buf, 0, numRead);
    }
    os.close();
}

public static void decrypt(String password, InputStream is, OutputStream os) throws Exception {

    SecretKeySpec keySpec = new SecretKeySpec(password(password), "TripleDES");
    Cipher cipher = Cipher.getInstance("TripleDES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);

    byte[] buf = new byte[8096];
    CipherInputStream cis = new CipherInputStream(is, cipher);
    int numRead = 0;
    while ((numRead = cis.read(buf)) >= 0) {
        os.write(buf, 0, numRead);
    }
    cis.close();
    is.close();
    os.close();
}

private static byte[] password(String password) {

    byte[] baseBytes = { (byte) 0x38, (byte) 0x5C, (byte) 0x8, (byte) 0x4C, (byte) 0x75, (byte) 0x77, (byte) 0x5B, (byte) 0x43,
            (byte) 0x1C, (byte) 0x1B, (byte) 0x38, (byte) 0x6A, (byte) 0x5, (byte) 0x0E, (byte) 0x47, (byte) 0x3F, (byte) 0x31,
            (byte) 0xF, (byte) 0xC, (byte) 0x76, (byte) 0x53, (byte) 0x67, (byte) 0x32, (byte) 0x42 };
    byte[] bytes = password.getBytes();
    int i = bytes.length;
    bytes = Arrays.copyOf(bytes, 24);
    if(i < 24){
        for(;i<24; i++){
            bytes[i] = baseBytes[i];
        }
    }
    return bytes;
}
