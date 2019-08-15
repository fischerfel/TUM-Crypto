String modulus = "<modulus string here...>";
String exponent = "<exponent string here...>";

BigInteger BIModulus = new BigInteger(1, Base64.decode(modulus));
BigInteger BIExponent = new BigInteger(1, Base64.decode(exponent));

RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(BIModulus, BIExponent);
PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, publicKey);

byte[] decodedBytes = cipher.doFinal(Base64.decode(signature));
