public static byte[] encrypt(PublicKey publicKey, String text) throws GeneralSecurityException {
    Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
    rsa.init(Cipher.ENCRYPT_MODE, publicKey);
    return rsa.doFinal(text.getBytes());
}
