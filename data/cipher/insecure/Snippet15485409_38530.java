private void readFile() {
  try {
    Cipher cipher = getCipher(2, "secret");
    DataInputStream dis;

    dis = new DataInputStream(new CipherInputStream(someInputStream, cipher));

    String field1 = dis.readUTF();
    String filed2 = dis.readUTF();
    dis.close();
  } catch (Exception e) { }
}

private Cipher getCipher(int mode, String password) throws Exception {
  Random random = new Random(43287234L);
  byte[] salt = new byte[8];
  random.nextBytes(salt);
  PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

  SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
  Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
  cipher.init(mode, pbeKey, pbeParamSpec);
  return cipher;
}
