public static void perturbacaoAES() throws Exception {
    Security.addProvider(new FlexiCoreProvider());

    Cipher cipher = Cipher.getInstance("AES128_CBC", "FlexiCore");
    KeyGenerator keyGen = KeyGenerator.getInstance("AES", "FlexiCore");
    SecretKey secKey = keyGen.generateKey();

    cipher.init(Cipher.ENCRYPT_MODE, secKey);

    FileInputStream fis = new FileInputStream("Zero.txt");
    FileOutputStream fos = new FileOutputStream("ZeroCifrado.txt");
    try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
        byte[] block = new byte[8];
        int i;
        while ((i = fis.read(block)) != -1) {
            cos.write(block, 0, i);
        }
    }
    fis.close();
    fos.close();

    byte[] newKey = secKey.getEncoded();

    newKey[0] ^= 1 << 1;

    secKey = new SecretKeySpec(newKey, "AES128_CBC");

    cipher.init(Cipher.ENCRYPT_MODE, secKey);

    FileInputStream nfis = new FileInputStream("Zero.txt");
    FileOutputStream nfos = new FileOutputStream("ZeroChaveCifrado.txt");
    try (CipherOutputStream cos = new CipherOutputStream(nfos, cipher)) {
        byte[] block = new byte[8];
        int i;
        while ((i = nfis.read(block)) != -1) {
            cos.write(block, 0, i);
        }
    }

}
