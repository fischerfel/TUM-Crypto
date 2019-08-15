public static byte[] decrypt(byte[] data) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    String fileName = "location/of/private_key/file";
    File f = new File(fileName);
    FileInputStream fis = new FileInputStream(f);
    DataInputStream dis = new DataInputStream(fis);
    byte[] keyBytes = new byte[(int)f.length()];
    dis.readFully(keyBytes);
    dis.close();
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey rsaPrivate = kf.generatePrivate(spec);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, rsaPrivate);
    return cipher.doFinal(data);
    }
