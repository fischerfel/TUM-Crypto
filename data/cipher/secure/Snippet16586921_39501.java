KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "SunJSSE");
generator.initialize(2048);
KeyPair keyPair = generator.generateKeyPair();

SecretKey sessionKey = new SecretKeySpec(new byte[16], "AES");

Cipher c = Cipher.getInstance("RSA", "SunJCE");
c.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
byte[] result1 = c.doFinal(sessionKey.getEncoded());

c.init(Cipher.WRAP_MODE, keyPair.getPublic());
byte[] result2 = c.wrap(sessionKey);

c.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
SecretKey sessionKey1 = (SecretKey) c.unwrap(result1, "AES",
    Cipher.SECRET_KEY);

c.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
SecretKey sessionKey2 = new SecretKeySpec(c.doFinal(result2), "AES");

System.out.println(Arrays.equals(sessionKey1.getEncoded(),
    sessionKey2.getEncoded()));
