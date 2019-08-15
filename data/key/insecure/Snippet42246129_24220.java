public static InputStream decryptInputStream(File encryptedFile) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec("MyDifficultPassw".getBytes(), "AES"));
    return new CipherInputStream(new FileInputStream(encryptedFile), cipher);
}
