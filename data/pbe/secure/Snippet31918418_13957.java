public String encrypt(final String message) {
  final byte[] salt = generateSalt();
  final Key key = createKey(salt);

  final Cipher encryptingCipher = createCipher(Cipher.ENCRYPT_MODE, key, salt);
  final byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
  final byte[] encryptedBytes = doFinal(encryptingCipher, messageBytes);
  final byte[] data = ArrayUtils.addAll(salt, encryptedBytes);
  return BaseEncoding.base64().encode(data);
}

private byte[] generateSalt() {
  final SecureRandom secureRandom = new SecureRandom();
  final byte[] salt = new byte[SALT_LENGTH];
  secureRandom.nextBytes(salt);
  return salt;
}

private Key createKey(final byte[] salt) {
  final PBEKeySpec spec = new PBEKeySpec(PASSWORD,
                                       salt,
                                       ITERATIONS,
                                       KEY_LENGTH);
  final SecretKey secretKey;
  try {
    secretKey = keyFactory.generateSecret(spec);
  } catch (final InvalidKeySpecException e) {
    throw new RuntimeException("Error creating SecretKey", e);
  }
  final SecretKeySpec result = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
  spec.clearPassword();
  return result;
}
