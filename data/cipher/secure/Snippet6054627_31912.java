private byte[] encRSA(byte[] in, java.security.PublicKey publicKey) {
    try {
        Cipher rsaCipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        rsaCipher.update(in);
        return rsaCipher.doFinal();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
