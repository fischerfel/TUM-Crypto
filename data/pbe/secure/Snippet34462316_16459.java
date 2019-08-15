public static String hashPassword(final String password, final String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    final char[] passwordChars = password.toCharArray();
    final byte[] saltBytes = salt.getBytes();
    final PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, 1000, 192);
    final SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    final byte[] hashedPassword = key.generateSecret(spec).getEncoded();
    return String.format("%x", new BigInteger(hashedPassword));
}
