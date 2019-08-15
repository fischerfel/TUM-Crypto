public final boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
  String algorithm = "PBKDF2WithHmacSHA1";
  int derivedKeyLength = 160;
  int iterations = 20000;
  KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
  SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

  byte[] encryptedPassword = f.generateSecret(spec).getEncoded();

  return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
}
