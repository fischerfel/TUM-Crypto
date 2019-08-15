String privKeyPEM = PRIVATEKEY.replace("-----BEGIN PRIVATE KEY-----\n", "")
.replace("-----END PRIVATE KEY-----", "");
byte [] encoded = Base64.decode(privKeyPEM, Base64.DEFAULT);
PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
KeyFactory kf = KeyFactory.getInstance("RSA");
privateKey = kf.generatePrivate(keySpec);
cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
