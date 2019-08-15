public void decrypt(File file, String output_file_path) throws FileNotFoundException, IOException, GeneralSecurityException {
    String hex_enc_key = "346a23652a46392b4d73257c67317e352e3372482177652c";
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec keySpec = new SecretKeySpec(HexParser.fromHexString(hex_enc_key), "AES");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(new File(output_file_path)), cipher);
    FileInputStream fis = new FileInputStream(file);
    doCopy(fis, cos);
}
