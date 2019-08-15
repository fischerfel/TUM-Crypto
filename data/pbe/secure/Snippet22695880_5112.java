/* Derive the key, given password and salt. */
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
/* Encrypt the message. */
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
try (OutputStream fos = Files.newOutputStream(output, StandardOpenOption.CREATE_NEW);
     CipherOutputStream os = new CipherOutputStream(fos, cipher);
     InputStream is = Files.newInputStream(input)) {
  byte[] buffer = new byte[4096];
  while (true) {
    int n = is.read(buffer);
    if (n < 0)
      break;
    os.write(buffer, 0, n);
  }
  os.flush();
}
