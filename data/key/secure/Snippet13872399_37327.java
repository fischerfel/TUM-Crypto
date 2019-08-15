  Mac localMac = getValidMac();
  localMac.init(new SecretKeySpec(str1.getBytes("UTF-8"), localMac.getAlgorithm()));
  byte[] arrayOfByte = localMac.doFinal(paramString.getBytes());
  BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
  String str4 = String.format("%0" + (arrayOfByte.length << 1) + "x", new Object[] { localBigInteger });
  str3 = str4;
  return str3;
