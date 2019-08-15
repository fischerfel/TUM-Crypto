public void encrypt(File file, String output_file_path) throws FileNotFoundException, IOException, GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
    SecretKeySpec keySpec = new SecretKeySpec(HexParser.fromHexString(db_enc_key), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    CipherInputStream cis = new CipherInputStream(new FileInputStream(file), cipher);
    FileOutputStream os = new FileOutputStream(new File(output_file_path));
    doCopy(cis, os);
    cis.close();
    os.close();
}
