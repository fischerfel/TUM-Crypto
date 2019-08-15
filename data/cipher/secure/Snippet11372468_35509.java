// Hardcoded values extracted from getModulus of a generated KeySpec.
private static BigInteger mod = new BigInteger("113...");
private static BigInteger exp = new BigInteger("217...");

private PublicKey hardCodedKey() {
    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
    KeyFactory keyFactory = null;
    PublicKey rsaKey = null;
    try {
        keyFactory = KeyFactory.getInstance("RSA");
        rsaKey = keyFactory.generatePublic(keySpec);
    } catch (Exception ex) {
        throw new IllegalStateException(ex);
    }
    return rsaKey;
}

private byte[] encrypt(PublicKey pubKey, byte[] plaintext) {
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(plaintext);
    } catch (Exception ex) {
        throw new IllegalStateException(ex);
    }
}
