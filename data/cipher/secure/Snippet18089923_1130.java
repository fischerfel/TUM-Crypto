// my clear text password
String clearTextPassword = "XXXXX";

// these are the actual values I get from the web service team
String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
String publicExponentString = "AQAB";

Base64 base64Encoder = new Base64();

String modulusHex = new String(Hex.encodeHex(modulusString.getBytes("UTF-8")));
String publicExponentHex = new String(Hex.encodeHex(publicExponentString.getBytes("UTF-8")));

BigInteger modulus = new BigInteger(modulusHex, 16);
BigInteger publicExponent = new BigInteger(publicExponentHex);

KeyFactory keyFactory = KeyFactory.getInstance("RSA");

RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);

String encodedEncryptedPassword = new String(base64Encoder.encode(cipher.doFinal(clearTextPassword.getBytes("UTF-8"))));
