int counter = 0;
String nextCounter = String.valueOf(++counter);
SecretKeySpec signingKey = new SecretKeySpec(macKey, "AES");
Mac mac = Mac.getInstance("HmacSHA256");
mac.init(signingKey);
byte[] counterMac = mac.doFinal(nextCounter.getBytes("UTF-8"));
String base64EncodedMac = DatatypeConverter.printBase64Binary(counterMac);
