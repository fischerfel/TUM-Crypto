String message = "..."; //this received from php
String privateKey = "..."; //this key was generated in android
byte[] privateKeyBytes = Base64.decode(privateKey);
PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
KeyFactory fact = KeyFactory.getInstance("RSA", "SC");
PrivateKey priv = fact.generatePrivate(keySpec);
Cipher rsaCipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "SC");
rsaCipher.init(Cipher.DECRYPT_MODE, priv);
byte[] messageDecryptBytes = rsaCipher.doFinal(message);
String messageDecrypt = new String(Base64.encode(messageDecryptBytes));
