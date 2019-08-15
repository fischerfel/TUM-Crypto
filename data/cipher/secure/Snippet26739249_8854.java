byte[] expBytes = Base64.decode(exponent.trim());
byte[] modBytes = Base64.decode(modulus.trim());

BigInteger modules = new BigInteger(1, modBytes);
BigInteger exponents = new BigInteger(1, expBytes);

KeyFactory factory = KeyFactory.getInstance("RSA");
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponents);
PublicKey pubKey = factory.generatePublic(pubSpec);
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] encrypted =cipher.doFinal(field.getBytes("UTF-16LE")); 
String string = new String(encrypted);
