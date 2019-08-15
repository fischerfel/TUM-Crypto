KeySpec keySpec = new X509EncodedKeySpec(new BigInteger(publicKey, 36).toByteArray());
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
Key decodedPublicKey = keyFactory.generatePublic(keySpec);
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, decodedPublicKey);     
byte a1[] = cipher.doFinal(z.getBytes());

KeySpec keySpec2 = new X509EncodedKeySpec(new BigInteger(publicKey, 36).toByteArray());
KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
Key decodedPublicKey2 = keyFactory2.generatePublic(keySpec2);
Cipher cipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher2.init(Cipher.ENCRYPT_MODE, decodedPublicKey2);
byte a2[] = cipher.doFinal(z.getBytes());
