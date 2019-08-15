IvParameterSpec localIvParameterSpec = new IvParameterSpec(new byte[] { 12, 34, 34, 11, 64, 23, 89, 27 });

DESKeySpec localDESKeySpec = new DESKeySpec(paramString2.getBytes());

SecretKey localSecretKey = SecretKeyFactory.getInstance("DES").generateSecret(localDESKeySpec);

Cipher localCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

localCipher.init(1, localSecretKey, localIvParameterSpec);

return new String(Base64.encode(localCipher.doFinal(paramString1.getBytes()), 0));
