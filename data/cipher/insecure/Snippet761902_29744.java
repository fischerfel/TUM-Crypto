public InputStream decrypt(InputStream is, byte[] password)
  throws GeneralSecurityException, IOException
{
  /* Parse the "salt" value from the stream. */
  byte[] header = new byte[16];
  for (int idx = 0; idx < header.length;) {
    int n = is.read(header, idx, header.length - idx);
    if (n < 0)
      throw new EOFException("File header truncated.");
    idx += n;
  }
  String magic = new String(header, 0, 8, "US-ASCII");
  if (!"Salted__".equals(magic))
    throw new IOException("Expected salt in header.");

  /* Compute the key and IV with OpenSSL's non-standard method. */
  SecretKey secret;
  IvParameterSpec iv;
  byte[] digest = new byte[32];
  try {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(password);
    md5.update(header, 8, 8);
    md5.digest(digest, 0, 16);
    md5.update(digest, 0, 16);
    md5.update(password);
    md5.update(header, 8, 8);
    md5.digest(digest, 16, 16);
    iv = new IvParameterSpec(digest, 24, 8);
    DESedeKeySpec keySpec = new DESedeKeySpec(digest);
    SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
    secret = factory.generateSecret(keySpec);
  }
  finally {
    Arrays.fill(digest, (byte) 0);
  }

  /* Initialize the cipher. */
  Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
  cipher.init(Cipher.DECRYPT_MODE, secret, iv);
  return new CipherInputStream(is, cipher);
}
