  Mac localMac = "HMAC-SHA256";
  String str1 = "a4d1b77bbb1a4a5ca695ad72c84b77e5";
  localMac.init(new SecretKeySpec(str1.getBytes("UTF-8"), localMac.getAlgorithm()));
  byte[] arrayOfByte = localMac.doFinal("{"_uid":"3396112","_csrftoken":"a23482932482sdsf4428","media_id":"616150302791211280_187036957"}");
  BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
  String str3 = String.format("%0" + (arrayOfByte.length << 1) + "x", new Object[] { localBigInteger });
  return str3;
