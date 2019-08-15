public void decryptDatabase(String k, String iv)

throws InvalidKeyException, InvalidAlgorithmParameterException,
        NoSuchAlgorithmException, NoSuchPaddingException, IOException {

    File extStore = Environment.getExternalStorageDirectory();
    FileInputStream fis = new FileInputStream(extStore
            + "/WhatsApp/Databases/msgstore.db.crypt7.nohdr");
    FileOutputStream fos = new FileOutputStream(extStore + "/Decrypted.db");

    SecretKeySpec sks = new SecretKeySpec(k.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, sks,
            new IvParameterSpec(iv.getBytes()));
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
