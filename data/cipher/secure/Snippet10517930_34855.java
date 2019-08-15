public static void main(String[] args) {


    Base64 base64 = new Base64();

    String TextStream = "this is the input text";
    byte[] Cipher;
    System.out.println("input:\n" + TextStream);
    Cipher = encrypt(TextStream);
    System.out.println("cipher:\n" + base64.encodeAsString(Cipher));
    System.out.println("decrypt:\n" + decrypt(Cipher));
}

private static byte[] encrypt(String Buffer) {
    try {

        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, getPrivateKey(PRIVATE_PATH));
        return rsa.doFinal(Buffer.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


private static String decrypt(byte[] buffer) {
    try {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.DECRYPT_MODE, getPrivateKey(PUBLIC_PATH));
        byte[] utf8 = rsa.doFinal(buffer);
        return new String(utf8, "UTF8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static PrivateKey getPrivateKey(String filename) throws Exception {
    File f = new File(filename);
    FileInputStream fis = new FileInputStream(f);
    DataInputStream dis = new DataInputStream(fis);
    byte[] keyBytes = new byte[(int) f.length()];
    dis.readFully(keyBytes);
    dis.close();

    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(spec);
}

public static PublicKey getPublicKey(String filename) throws Exception {
    File f = new File(filename);
    FileInputStream fis = new FileInputStream(f);
    DataInputStream dis = new DataInputStream(fis);
    byte[] keyBytes = new byte[(int) f.length()];
    dis.readFully(keyBytes);
    dis.close();

    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(spec);
}
