String key = "test";
String data = "test";
String algorithm = "HmacSHA1";
Charset charset = Charset.forName("utf-8");
SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), algorithm);
Mac mac = Mac.getInstance(algorithm);
mac.init(signingKey);
return new String(Base64.encodeBase64(mac.doFinal(data.getBytes(charset))), charset);
