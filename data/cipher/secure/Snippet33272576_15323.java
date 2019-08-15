PKCS8EncodedKeySpec spec = new PKCSEncodedKeySpec (bytearray);
KeyFactory factory = KeyFactory.getInstance("RSA");
PrivateKey privkey = factory.generatePrivate (spec);
// use privkey in a Cipher.getInstance("RSA") to decrypt or sign
