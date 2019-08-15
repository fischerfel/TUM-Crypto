public static byte[] getEncryptedPassword(String password, byte[] salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 64;
    int iterations = 10000;
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
    SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
    return f.generateSecret(spec).getEncoded();
}
