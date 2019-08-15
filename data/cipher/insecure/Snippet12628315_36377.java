PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(key);
KeyFactory factory = KeyFactory.getInstance("EC", "JsafeJCE");
PrivateKey privateKey = factory.generatePrivate(privKeySpec);
Cipher eciesDecrypter = Cipher.getInstance("ECIES/SHA1/HMACSHA1", "JsafeJCE");
