byte[] enc = DatatypeConverter.parseBase64Binary(clientData);
Cipher rsa = Cipher.getInstance("RSA");
rsa.init(Cipher.DECRYPT_MODE, pk);
byte[] dec = rsa.doFinal(enc);
String out = new String(dec, "UTF8");
