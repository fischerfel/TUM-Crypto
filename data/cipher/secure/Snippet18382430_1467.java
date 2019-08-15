RSAPrivateKeySpec privateKeySpec =
   new RSAPrivateKeySpec(new BigInteger(modulus, 10), new BigInteger(secretExponent, 10));
RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);

Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
StringBuilder externalKeyBuf =
    new StringBuilder(new String(cipher.doFinal(new BigInteger(encExternalKey, 16).toByteArray())));
String externalKey = externalKeyBuf.reverse().toString().trim();
