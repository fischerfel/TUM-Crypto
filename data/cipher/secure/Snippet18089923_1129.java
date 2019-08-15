// my clear text password
String clearTextPassword = "XXXXX";

// these values are provided by the web service team
String modulusString = "...";
String publicExponentString = "...";

BigInteger modulus = new BigInteger(1, Base64.decodeBase64(modulusString.getBytes("UTF-8")));
BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(publicExponentString.getBytes("UTF-8")));

KeyFactory keyFactory = KeyFactory.getInstance("RSA");

RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);

String encodedEncryptedPassword = new String(Base64.encodeBase64(cipher.doFinal(clearTextPassword.getBytes("UTF-8"))));
