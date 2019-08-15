byte[] secretKey = secretAccessKey.getBytes("UTF-8");
SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA256");
Mac mac = Mac.getInstance("HmacSHA256");
mac.init(signingKey);
byte[] bytes = data.getBytes("UTF-8");
byte[] rawHmac = mac.doFinal(bytes);
String result = javax.xml.bind.DatatypeConverter.printBase64Binary(rawHmac);
