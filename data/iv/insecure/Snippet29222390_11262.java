InputStream privateKeyFileInputStream = Check.class.getClassLoader().getResourceAsStream("rsa_privatekey.key"); 
rsaPrivateKey = new byte[privateKeyFileInputStream.available()];
privateKeyFileInputStream.read(rsaPrivateKey);

PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey);
KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
PrivateKey privKey = keyFactory.generatePrivate(privateKeySpec);

Cipher cipher1 = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", "BC");
cipher1.init(Cipher.DECRYPT_MODE, privKey);

byte[] encryptedMessage = Base64.decodeBase64(aesPrivateKeyEnc.getBytes());
aesPrivateKey = cipher1.doFinal(encryptedMessage);

IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
SecretKeySpec key = new SecretKeySpec(aesPrivateKey, "AES");
Cipher cipher2 = Cipher.getInstance("AES/CBC/NoPadding", "BC");
cipher2.init(Cipher.DECRYPT_MODE, key, ivSpec);

byte[] cipherTextBytes = Base64.decodeBase64(cipherText);        
byte[] decryptedMessage = cipher2.doFinal(cipherTextBytes);

String message = new String(decryptedMessage, "UTF8");
