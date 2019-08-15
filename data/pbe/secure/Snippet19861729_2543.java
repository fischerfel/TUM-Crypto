private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
    throws NoSuchAlgorithmException, InvalidKeySpecException
{
    PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    return skf.generateSecret(spec).getEncoded();
}
