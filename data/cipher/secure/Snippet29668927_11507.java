byte[] cipherTextBytes = DatatypeConverter.parseBase64Binary(cipherText);
byte[] privateKeyBytes = DatatypeConverter.parseBase64Binary(privateKeyStr);

KeyFactory kf = KeyFactory.getInstance("RSA");
PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privateKeyBytes);
PrivateKey privateKey = kf.generatePrivate(ks);

Cipher c = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
c.init(Cipher.DECRYPT_MODE, privateKey, new OAEPParameterSpec("SHA-256",
        "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
byte[] plainTextBytes = c.doFinal(cipherTextBytes);
String plainText = new String(plainTextBytes);

System.out.println(plainText);
