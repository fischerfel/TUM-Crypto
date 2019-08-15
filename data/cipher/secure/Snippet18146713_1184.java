String modulusString = "hm2oRCtP6usJKYpq7o1K20uUuL11j5xRrbV4FCQhn/JeXLT21laKK9901P69YUS3bLo64x8G1PkCfRtjbbZCIaa1Ci/BCQX8nF2kZVfrPyzcmeAkq4wsDthuZ+jPInknzUI3TQPAzdj6gim97E731i6WP0MHFqW6ODeQ6Dsp8pc=";
String publicExponentString = "AQAB";

byte[] modulusBytes = Base64.decodeBase64(modulusString);
byte[] exponentBytes = Base64.decodeBase64(publicExponentString);
BigInteger modulus = new BigInteger(1, modulusBytes);
BigInteger publicExponent = new BigInteger(1, exponentBytes);

RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey pubKey = fact.generatePublic(rsaPubKey);
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] plainBytes = clearTextPassword.getBytes("UTF-16LE");
byte[] cipherData = cipher.doFinal(plainBytes);
String encryptedStringBase64 = Base64.encodeBase64String(cipherData);
