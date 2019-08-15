keyFactory = KeyFactory.getInstance("RSA", "BC");

pubKeySpec = new RSAPublicKeySpec(publicModulus,
                    publicExponent);
key = (RSAPublicKey) keyFactory
                    .generatePublic(pubKeySpec);

cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] bytes = "hello".getBytes("ISO-8859-1");
byte[] encrypted = cipher.doFinal(bytes);

pubKeySpec = new RSAPublicKeySpec(privateModulus,
                    privateExponent);
key = (RSAPublicKey) keyFactory
                    .generatePublic(pubKeySpec);

cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] utf8 = cipher.doFinal(encrypted);
String plainString = new String(utf8, "ISO-8859-1");
