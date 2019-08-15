PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b64.decodeBuffer(key));
PrivateKey privateKey = kf.generatePrivate(keySpec);
Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] myData = cipher.doFinal(byteData, 0, 256);
