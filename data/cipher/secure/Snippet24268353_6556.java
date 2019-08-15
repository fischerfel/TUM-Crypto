private static boolean testVector(String algorithm, String key, String iv,
        String vector, String ciphertext) throws GeneralSecurityException {

    Cipher c = Cipher.getInstance(algorithm);
    SecretKey k = new SecretKeySpec(DatatypeConverter.parseHexBinary(key),
            "AES");

    if (iv == null) {
        c.init(Cipher.ENCRYPT_MODE, k);
    } else {
        c.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(
                DatatypeConverter.parseHexBinary(iv)));
    }

    byte[] result = c.doFinal(DatatypeConverter.parseHexBinary(vector));
    return Arrays.equals(result,
            DatatypeConverter.parseHexBinary(ciphertext));
}
