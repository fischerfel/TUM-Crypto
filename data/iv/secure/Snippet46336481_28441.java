File f1 = new File(inputFile);
if (f1.exists()) {
  CommanFunction.writeErrorLogInFile(MyApplication.getmContext(), 
    "LodingMusic Call Start --> doCrypto() " + f1.getAbsolutePath());
  inputStream = new FileInputStream(inputFile);
  outputStream = new FileOutputStream(outputFile);
  byte[] iv = key.getBytes("UTF-8");
  IvParameterSpec ivParams = new IvParameterSpec(iv);
  Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
  Cipher cipher = Cipher.getInstance(TRANSFORMATION);
  cipher.init(cipherMode, secretKey, ivParams);
  CipherInputStream cis = new CipherInputStream(inputStream, cipher);
  int b;
  byte[] d = new byte[131072];
  while ((b = cis.read(d)) != -1) {
    outputStream.write(d, 0, b);
  }
  outputStream.flush();
  outputStream.close();
  cis.close();
  CommanFunction.writeErrorLogInFile(MyApplication.getmContext(), 
    "LodingMusic Call End --> doCrypto() " + f1.getAbsolutePath());
}
