BigInteger modulus = new BigInteger("F56D...", 16);
BigInteger pubExp = new BigInteger("010001", 16);

KeyFactory keyFactory = KeyFactory.getInstance("RSA");
RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] cipherData = cipher.doFinal(text.getBytes());
