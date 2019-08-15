public static byte[] getEncryptedPassword(String password, byte[] salt,  int iterations,  int derivedKeyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength * 8);

    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

    return f.generateSecret(spec).getEncoded();
}
