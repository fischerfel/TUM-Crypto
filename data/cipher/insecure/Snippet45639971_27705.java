  public static void encrypt(SecretKey key, InputStream in, OutputStream out)
      throws NoSuchAlgorithmException, InvalidKeyException,
      NoSuchPaddingException, IOException {
    // Create and initialize the encryption engine
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.ENCRYPT_MODE, key);

    // Create a special output stream to do the work for us
    CipherOutputStream cos = new CipherOutputStream(out, cipher);

    // Read from the input and write to the encrypting output stream
    byte[] buffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
      cos.write(buffer, 0, bytesRead);
    }
    cos.close();

    // For extra security, don't leave any plaintext hanging around memory.
    java.util.Arrays.fill(buffer, (byte) 0);
  }

  /**
   * Use the specified TripleDES key to decrypt bytes ready from the input
   * stream and write them to the output stream. This method uses uses Cipher
   * directly to show how it can be done without CipherInputStream and
   * CipherOutputStream.
   */
  public static void decrypt(SecretKey key, InputStream in, OutputStream out)
      throws NoSuchAlgorithmException, InvalidKeyException, IOException,
      IllegalBlockSizeException, NoSuchPaddingException,
      BadPaddingException {
    // Create and initialize the decryption engine
    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(Cipher.DECRYPT_MODE, key);

    // Read bytes, decrypt, and write them out.
    byte[] buffer = new byte[2048];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
      out.write(cipher.update(buffer, 0, bytesRead));
    }

    // Write out the final bunch of decrypted bytes
    out.write(cipher.doFinal());
    out.flush();
  }
