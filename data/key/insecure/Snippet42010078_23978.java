    public String decode(AssetManager assets) throws GeneralSecurityException, UnsupportedEncodingException {
    String encryptedText = null;
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    SecretKeySpec key = new SecretKeySpec("testPass".getBytes(), "Blowfish");
    try {
        byte[] encryptedBytes = readFile("RequestManager/RM.dat", assets).getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(org.bouncycastle.util.encoders.Base64.decode(encryptedBytes));
        return new String(decrypted);
    } catch (Exception e) {
        e.printStackTrace();
        return null; }
}
