KeyFactory keyFactory = KeyFactory.getInstance("RSA");
KeySpec keySpec = new PKCS8EncodedKeySpec(keyRawBytes);
java.security.Key key = keyFactory.generatePrivate(keySpec);
javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
cipher.init(1, key);
encryptedBytes = cipher.doFinal(data);
