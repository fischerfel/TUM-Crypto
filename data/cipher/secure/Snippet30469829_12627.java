public static String encodeRSA(String keyModulus, String keyExponent,
        String data) {
    try {

        byte btMod[] = Base64.decode(keyModulus);
        byte btExp[] = Base64.decode(keyExponent);

        BigInteger modulus = new BigInteger(1, btMod);
        BigInteger pubExp = new BigInteger(1, btExp);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherData = cipher.doFinal(data.getBytes());
        String tmp = new String(Base64.encode(cipherData));

        System.out.println(tmp);

        return tmp;
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return "";
}
