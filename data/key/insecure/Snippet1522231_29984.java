String pub = 'my_public_key';
BASE64Encoder encoder = new BASE64Encoder();
Mac sha1Mac = Mac.getInstance("HmacSHA1");
SecretKeySpec publicKeySpec = new SecretKeySpec(pub.getBytes(), "HmacSHA1");
sha1Mac.init(publicKeySpec);
byte[] publicBytes = sha1Mac.doFinal(subscriptionID.getBytes());
String publicDigest = encoder.encodeBuffer(publicBytes);
publicDigest = publicDigest.replaceAll("\n", "");
