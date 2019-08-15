SecretKey secret = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters param = cipher.getParameters();
/* In addition to ciphertext in "cos", recipient needs IV. */
byte[] iv = param.getParameterSpec(IvParameterSpec.class).getIV();
CipherOutputStream cos = new CipherOutputStream(output, cipher);
byte[] buf = new byte[2048];
while (true) {
  int n = input.read(buf, 0, buf.length);
  if (n < 0)
    break;
  cos.write(buf, 0, n);
}
cos.flush();
