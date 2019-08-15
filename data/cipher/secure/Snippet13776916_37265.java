KeyGenerator keyGen  = KeyGenerator.getInstance("AES");
keyGen.init(256, new SecureRandom());
SecretKey secretKey = keyGen.generateKey();

byte[] publicKeyBytes = getBytes(publicKey.getKey());
PublicKey rsaKey = KeyFactory.getInstance("RSA")
    .generatePublic(new X509EncodedKeySpec(publicKeyBytes));

Cipher cipher = Cipher.getInstance(RSA);
cipher.init(Cipher.ENCRYPT_MODE, rsaKey);

String keyEncoded = getString(key);

return getString(encryptedKeyBytes));
