public String encryptText(String plainText) throws Exception{

  byte[] plaintext = plainText.getBytes();
  byte[] tdesKeyData = key;
  byte[] myIV = initializationVector;

  Cipher c3des = Cipher.getInstance(""DESede/CBC/NoPadding"");
  SecretKeySpec    myKey = new SecretKeySpec(tdesKeyData, "DESede");
  IvParameterSpec ivspec = new IvParameterSpec(myIV);
     c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
  byte[] cipherText = c3des.doFinal(plaintext);
  sun.misc.BASE64Encoder obj64=new sun.misc.BASE64Encoder();
  return obj64.encode(cipherText);
 }
