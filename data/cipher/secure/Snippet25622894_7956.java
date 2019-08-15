Cipher rsa;
rsa = Cipher.getInstance("RSA");
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pkey.getBytes());
PublicKey pk = keyFactory.generatePublic(publicKeySpec);
rsa.init(Cipher.DECRYPT_MODE, pk);
byte[] cipherDecrypt = rsa.doFinal(encryptedText.getBytes());
