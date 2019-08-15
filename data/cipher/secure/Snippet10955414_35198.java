Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
PublicKey pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubs));
cipher.init(Cipher.ENCRYPT_MODE, pk);
byte[] cipherBytes = cipher.doFinal(plainArray);
