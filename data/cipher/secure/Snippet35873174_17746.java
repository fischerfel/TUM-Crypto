static final String RSA_ALGO = "RSA/ECB/PKCS1Padding";
//  static final String RSA_ALGO = "RSA";

private void _testCrypto2() throws Exception {
  KeyPairGenerator keyGen;
  KeyPair          keys;
  byte[]           encrypted;
  byte[]           decrypted;
  String           input;
  String           output;

  keyGen = KeyPairGenerator.getInstance("RSA");
  keyGen.initialize(2048);
  keys = keyGen.generateKeyPair();

  input = "foobar";

  // Plain crypto.
  encrypted = this.RSAEncrypt(input, keys.getPublic());
  output    = this.RSADecrypt(encrypted, keys.getPrivate());

  // Streaming crypto.
  encrypted = this.RSAEncryptStream(input, keys.getPublic());
  output    = this.RSADecryptStream(encrypted, keys.getPrivate());
}

public byte[] RSAEncrypt(final String plain, PublicKey _publicKey) throws Exception {
  byte[] encryptedBytes;
  Cipher cipher;

  cipher = Cipher.getInstance(RSA_ALGO);
  cipher.init(Cipher.ENCRYPT_MODE, _publicKey);
  encryptedBytes = cipher.doFinal(plain.getBytes());

  return encryptedBytes;
}

public String RSADecrypt(final byte[] encryptedBytes, PrivateKey _privateKey) throws Exception {
  Cipher cipher;
  byte[] decryptedBytes;
  String decrypted;

  cipher = Cipher.getInstance(RSA_ALGO);
  cipher.init(Cipher.DECRYPT_MODE, _privateKey);

  decryptedBytes = cipher.doFinal(encryptedBytes);
  decrypted      = new String(decryptedBytes);

  return decrypted;
}

public byte[] RSAEncryptStream(final String _plain, PublicKey _publicKey) throws Exception {
  Cipher                cipher;
  InputStream           in;
  ByteArrayOutputStream out;
  int                   numBytes;
  byte                  buffer[] = new byte[0xffff];

  in     = new ByteArrayInputStream(_plain.getBytes());
  out    = new ByteArrayOutputStream();
  cipher = Cipher.getInstance(RSA_ALGO);
  cipher.init(Cipher.ENCRYPT_MODE, _publicKey);

  try {
    in = new CipherInputStream(in, cipher);
    while ((numBytes = in.read(buffer)) != -1) {
      out.write(buffer, 0, numBytes);
    }
  }
  finally {
    in.close();
  }

  return out.toByteArray();
}

public String RSADecryptStream(final byte[] _encryptedBytes, PrivateKey _privateKey) throws Exception {
  Cipher                cipher;
  InputStream           in;
  ByteArrayOutputStream out;
  int                   numBytes;
  byte                  buffer[] = new byte[0xffff];

  in     = new ByteArrayInputStream(_encryptedBytes);
  out    = new ByteArrayOutputStream();  
  cipher = Cipher.getInstance(RSA_ALGO);
  cipher.init(Cipher.DECRYPT_MODE, _privateKey);

  try {
    in = new CipherInputStream(in, cipher);
    while ((numBytes = in.read(buffer)) != -1) {
      out.write(buffer, 0, numBytes);
    }
  }
  finally {
    in.close();
  }

  return new String(out.toByteArray());
}
