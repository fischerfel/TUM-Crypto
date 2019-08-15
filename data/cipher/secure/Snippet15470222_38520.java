public static void main(String[] args) throws Exception {

    KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    Cipher cipher = Cipher.getInstance("RSA");

    cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());

    FileInputStream fis;
    FileOutputStream fos;
    CipherOutputStream  cos;

    fis = new FileInputStream("C:/temp/a.xlsx");
    fos = new FileOutputStream("C:/temp/b.xlsx");

    cos = new CipherOutputStream (fos, cipher);

    byte[] block = new byte[8];
    int i;
    while ((i = fis.read(block)) != -1) {
        cos.write(block, 0, i);
    }
    cos.close();
    fos.close();



    cipher.init(Cipher.DECRYPT_MODE, keys.getPrivate());
    CipherInputStream cis1, cis2;
    fis = new FileInputStream("c:/temp/b.xlsx");
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    fos = new FileOutputStream("c:/temp/c.xlsx");

    while ((i = cis.read(block)) != -1) {
        fos.write(block, 0, i);
    }
    fos.close();
    fis.close();
    cis.close();
}
