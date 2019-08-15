KeyFactory factory rsa = KeyFactory.getInstance("RSA");
BigInteger n = ... ; /* modulus */
BigInteger e = ... ; /* public exponent */
RSAPublicKeySpec spec = new RSAPublicKeySpec(n, e);
RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
Cipher enc = Cipher.getInstance("RSA");
enc.init(Cipher.WRAP_MODE, pub);
byte[] encryptedContentKey = enc.wrap(secret);
