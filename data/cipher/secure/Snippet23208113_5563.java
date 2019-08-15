final Provider bc = new BouncyCastleProvider();

// generating the key from modulus & private exponent
KeyFactory rsaFactory = KeyFactory.getInstance("RSA", bc);
RSAPrivateKeySpec spec = new RSAPrivateKeySpec(key.getModulus(), key.getPrivateExponent());
RSAPrivateKey testKey = (RSAPrivateKey) rsaFactory.generatePrivate(spec);

// using it in a raw cipher
Cipher c= Cipher.getInstance("RSA/ECB/NoPadding", bc);
c.init(Cipher.DECRYPT_MODE, testKey);
c.doFinal(new byte[] {(byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, });
