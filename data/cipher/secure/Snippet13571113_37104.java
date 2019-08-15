public static void main(String[] args) throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    kpg.initialize(1024);
    KeyPair keyPair = kpg.generateKeyPair();
    PrivateKey privKey = keyPair.getPrivate();
    PublicKey pubKey = keyPair.getPublic();

    // Encrypt
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    String test = "My test string";
    String ciphertextFile = "ciphertextRSA.txt";
    InputStream fis = new ByteArrayInputStream(test.getBytes("UTF-8"));

    FileOutputStream fos = new FileOutputStream(ciphertextFile);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    byte[] block = new byte[32];
    int i;
    while ((i = fis.read(block)) != -1) {
        cos.write(block, 0, i);
    }
    cos.close();

    // Decrypt
    String cleartextAgainFile = "cleartextAgainRSA.txt";

    cipher.init(Cipher.DECRYPT_MODE, privKey);

    fis = new FileInputStream(ciphertextFile);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    fos = new FileOutputStream(cleartextAgainFile);

    while ((i = cis.read(block)) != -1) {
        fos.write(block, 0, i);
    }
    fos.close();
}
