public byte[] encryptData(byte[] data, String key) {
    byte[] encrypted = null;

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    byte[] keyBytes = key.getBytes();

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

    try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        encrypted = new byte[cipher.getOutputSize(data.length)];
        int ctLength = cipher.update(data, 0, data.length, encrypted, 0);
        ctLength += cipher.doFinal(encrypted, ctLength);
    } catch (Exception e) {
        logger.log(Level.SEVERE, e.getMessage());
    } finally {
        return encrypted;
    }
}
