 InputStream in = new FileInputStream(derkeyfilename);
 byte[] privKeyBytes = new byte[in.available()]; 
 in.read(privKeyBytes);
 KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
 PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
 private RSAPrivateKey myPrivateKey = (RSAPrivateKey) rsaKeyFac.generatePrivate(encodedKeySpec);

 MessageDigest md = MessageDigest.getInstance("MD5", "BC");
 byte[] digest = md.digest(msg);
 Signature sig = Signature.getInstance("MD5withRSA", "BC");
 sig.initSign(myPrivateKey);
 sig.update(digest);
 byte[] signature = sig.sign();
 byte[] base64 = Base64.encodeBase64(signature);
 String signature = new String(base64);
