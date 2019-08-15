SecretKeySpec keySpec = new SecretKeySpec("foo".getBytes(), "HmacMD5");
Mac mac = Mac.getInstance(keySpec.getAlgorithm());
mac.init(keySpec);
String result = new BASE64Encoder().encode(mac.doFinal("bar".getBytes()));
