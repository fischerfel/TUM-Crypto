private Key createKey(final byte[] salt) {
    final PBEKeySpec spec = new PBEKeySpec(KEY,
                                           salt,
                                           20000,
                                           256);
    final SecretKey secretKey;
    secretKey = keyFactory.generateSecret(spec);        
    return new SecretKeySpec(secretKey.getEncoded(), 'AES');
  }

public String encrypt(final String message) {
    final byte[] salt = secureRandom.nextBytes(SALT_LENGTH);
    final Key key = createKey(salt);

    final Cipher encryptingCipher = createCipher(Cipher.ENCRYPT_MODE, key, salt);
    final byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
    final byte[] encryptedBytes = doFinal(encryptingCipher, messageBytes);
    final byte[] data = ArrayUtils.addAll(salt, encryptedBytes);
    return BaseEncoding.base64().encode(data);
  }
