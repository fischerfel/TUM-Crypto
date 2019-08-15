byte[] keyBytes = Base64.decodeBase64(rsa_1024_public_key);
      // rsa_1024_public key is a constant String

Cipher c = Cipher.getInstance("RSA");

PublicKey publicKey =
   KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));

c.init(Cipher.ENCRYPT_MODE, publicKey);

return c.doFinal(password.getBytes());
