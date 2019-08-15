void decrypt(File file1, String nama) throws IOException, NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException {

    md5 hash = new md5();
    String sapi = hash.md5(nama);

    FileInputStream fis = new FileInputStream(file1+ "/" + sapi);

    FileOutputStream fos = new FileOutputStream(file1 + "/decrypted.json");

    SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
            "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];
    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }

    fos.flush();
    fos.close();
    cis.close();

}
