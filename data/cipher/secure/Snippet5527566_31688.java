byte[] expBytes = Base64.decodeBase64(exponentElem.getText().trim()));
byte[] modBytes = Base64.decodeBase64(modulusElem.getText().trim());
byte[] dBytes = Base64.decodeBase64(dElem.getText().trim());

BigInteger modules = new BigInteger(1, modBytes);
BigInteger exponent = new BigInteger(1, expBytes);
BigInteger d = new BigInteger(1, dBytes);

KeyFactory factory = KeyFactory.getInstance("RSA");
Cipher cipher = Cipher.getInstance("RSA");
String input = "test";

RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
PublicKey pubKey = factory.generatePublic(pubSpec);
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
System.out.println("encrypted: " + new String(encrypted));

RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
PrivateKey privKey = factory.generatePrivate(privSpec);
cipher.init(Cipher.DECRYPT_MODE, privKey);
byte[] decrypted = cipher.doFinal(encrypted);
System.out.println("decrypted: " + new String(decrypted));
