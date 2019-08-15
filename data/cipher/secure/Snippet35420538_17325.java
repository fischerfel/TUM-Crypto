    public static String decrypt(byte[] encryptedMessage) {

    try {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        PrivateKey ourKey = getKey("resources/privateKey.der");
        rsa.init(Cipher.DECRYPT_MODE, ourKey);
        byte[] utf8 = rsa.doFinal(encryptedMessage);
        return new String(utf8, "UTF8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static PrivateKey getKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        File f = new File(filePath);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
