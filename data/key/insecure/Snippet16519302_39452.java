SecretKeySpec keySpec = new SecretKeySpec("secretkey".getBytes(), "HmacMD5");
Mac mac = Mac.getInstance("HmacMD5");
mac.init(keySpec);
byte[] hashBytes = mac.doFinal("text2crypt".getBytes());
return Hex.encodeHexString(hashBytes);
