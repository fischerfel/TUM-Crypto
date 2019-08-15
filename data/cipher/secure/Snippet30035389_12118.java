PrivateKey readKeyFromFileDecrypt(String keyFileName) throws IOException {
  InputStream in =
    cli.class.getResourceAsStream(keyFileName);
  ObjectInputStream oin =
    new ObjectInputStream(new BufferedInputStream(in));
  try {
    BigInteger m = (BigInteger) oin.readObject();
    BigInteger e = (BigInteger) oin.readObject();
    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey priKey = fact.generatePrivate(keySpec);
    return priKey;
  } catch (Exception e) {
    throw new RuntimeException("Spurious serialisation error", e);
  } finally {
    oin.close();
  }
}
//similar function readKeyFromFileEncrypt
public byte[] rsaEncrypt(String keyFileName,byte[] data,String key)throws Exception {
if(key.equals("public")){
  PublicKey pubKey = readKeyFromFileEncrypt(keyFileName);
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.ENCRYPT_MODE, pubKey);
}
else if(key.equals("private"))
{
  PrivateKey priKey = readKeyFromFileDecrypt(keyFileName);
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.ENCRYPT_MODE, priKey);
}
  byte[] cipherData = cipher.doFinal(data);
  return cipherData;
}
public byte[] rsaDecrypt(String keyFileName,byte[] data,String key)throws Exception{
if(key.equals("public")){
  PublicKey pubKey = readKeyFromFileEncrypt(keyFileName);
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.DECRYPT_MODE, pubKey);
}
else if(key.equals("private"))
{
  PrivateKey priKey = readKeyFromFileDecrypt(keyFileName);
  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.DECRYPT_MODE, priKey);

}
  byte[] originalData = cipher.doFinal(data);
  return originalData;
}
