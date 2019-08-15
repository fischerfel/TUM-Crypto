// 1) key
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(myModulus, myPublicExponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
Key pubKey = fact.generatePublic(keySpec);

// 2) cypher
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, keySpec);

// 3) use cypher to decode my block to an output stream
