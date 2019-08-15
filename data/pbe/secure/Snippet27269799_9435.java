private static byte[] Rfc2898DeriveBytes(String password, byte[] salt, int iterations) throws GeneralSecurityException {

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, Iterations, 256);
    Key secretKey = factory.generateSecret(pbeKeySpec);
    byte[] rawKeyData = secretKey.getEncoded();
    return rawKeyData;
}
