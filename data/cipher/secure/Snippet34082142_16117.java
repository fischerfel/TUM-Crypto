String pubKeyPem = PUBKEY_X509.replace("-----BEGIN PUBLIC KEY-----\n", "")
.replace("-----END PUBLIC KEY-----", "");
byte [] encoded = Base64.decode(pubKeyPem, Base64.DEFAULT);
X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
KeyFactory kf = KeyFactory.getInstance("RSA");
privateKey = kf.generatePrivate(keySpec);
cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
