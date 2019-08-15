/**
   * Decrypt text using private key.
   *
   * @param text
   *          :encrypted text
   * @param key
   *          :The private key
   * @return plain text
   * @throws java.lang.Exception
   */
  public static byte[] RSADecrypt(final byte[] text, final PrivateKey key) {

    byte[] plainText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      plainText = cipher.doFinal(text);

    }
    catch (final Exception ex) {
      ex.printStackTrace();
    }

    return plainText;
  }

  /**
   * Encrypt the plain text using public key.
   *
   * @param text
   *          : original plain text
   * @param key
   *          :The public key
   * @return Encrypted text
   * @throws java.lang.Exception
   */
  public static byte[] RSAEncrypt(final byte[] input, final PublicKey key) {

    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(input);
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
    // return Arrays.copyOfRange(cipherText, 1, cipherText.length); // Ignore the first byte (sign byte)
    return cipherText;
  }
