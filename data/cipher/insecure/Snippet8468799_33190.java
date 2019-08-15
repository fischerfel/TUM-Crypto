// requires commons-io and commons-codec
public void testDecryption() throws Exception {
    File encryptedFile = new File("encrypted.txt");
    String password = "secret";

    byte[] base64EncryptedBytes = FileUtils.readFileToByteArray(encryptedFile);
    byte[] encryptedBytes = new Base64().decode(base64EncryptedBytes);

    SecretKeySpec blowfishKey = new SecretKeySpec(password.getBytes("ASCII"), "Blowfish");
    Cipher blowfishCipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
    blowfishCipher.init(Cipher.DECRYPT_MODE, blowfishKey);
    byte[] decryptedContent = blowfishCipher.doFinal(encryptedBytes);

    System.out.println(new String(decryptedContent));
}
