public byte[] decrypt(byte[] data) {
    try {
        PrivateKey privateKey = this.keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        this.server.logDebug("Decrypting data: " + this.bytesToHex(data));
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    } catch (Exception e) {
        this.server.logException(e);
        return new byte[0];
    }
}
