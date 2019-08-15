public static byte[] getSignature(String keyFileName, byte[] secretKey){
    byte[] signature = null;
    try {
        PublicKey pubKey = readKeyFromFile(keyFileName);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        signature = cipher.doFinal(secretKey);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return signature;
}
private static PublicKey readKeyFromFile(String keyFileName) throws IOException {
    InputStream in = new FileInputStream(keyFileName);
    DataInputStream din = new DataInputStream(in);
    try {
        BigInteger m = BigInteger.valueOf(din.readLong());
        BigInteger e = BigInteger.valueOf(din.readLong());
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) fact.generatePublic(keySpec);
        return pubKey;
    } catch (Exception e) {
        throw new RuntimeException("Spurious serialisation error", e);
    } finally {
        din.close();
    }
}
