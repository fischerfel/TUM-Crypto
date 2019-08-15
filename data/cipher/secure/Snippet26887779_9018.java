// *** setup private key

RSAPrivateKeySpec privateRPKS
= new RSAPrivateKeySpec(new BigInteger(gModulusPlainS, 16), new BigInteger(privateExponentPlainS, 16));
KeyFactory keyFactoryKF = KeyFactory.getInstance("RSA");
RSAPrivateKey gPrivateKeyRPK = (RSAPrivateKey) keyFactoryKF.generatePrivate(privateRPKS);

// *** setup cipher
Cipher cipherC = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipherC.init(Cipher.DECRYPT_MODE, gPrivateKeyRPK);

// *** decrypt hex-encoded cipherTxS
byte[] baCipherText = hexToBin(cipherTxS.getBytes());
byte[] baPlainText2 = cipherC.doFinal(baCipherText);
String decryptedTextS = new String(baPlainText2);
