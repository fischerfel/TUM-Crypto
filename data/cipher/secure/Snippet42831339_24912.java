Cipher asymmetricCipher = null;
asymmetricCipher = Cipher.getInstance("RSA");
X509EncodedKeySpec publicKeySpec128 = new X509EncodedKeySpec(key128block);
X509EncodedKeySpec publicKeySpec74 = new X509EncodedKeySpec(key74block);

KeyFactory keyFactory = KeyFactory.getInstance("RSA");
Key key = keyFactory.generatePublic(publicKeySpec128);
asymmetricCipher.init(Cipher.DECRYPT_MODE, key);

byte[] plainText = asymmetricCipher.doFinal(topBlocksData[0]);
