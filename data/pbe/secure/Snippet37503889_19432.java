public static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {

    try {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKey key = skf.generateSecret(spec);
        byte[] res = key.getEncoded();
        return res;

    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new RuntimeException(e);
    }
}
