KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
generator.initialize(1024);
KeyPair keyPair = generator.generateKeyPair();
SecretKey zmkKey = new SecretKeySpec(new byte[16], "AES");

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.WRAP_MODE, zmkKey, new IvParameterSpec(new byte[16]));
byte[] encryptedPrivateKey = c.wrap(keyPair.getPrivate());

AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
algorithmParameters.init(new IvParameterSpec(new byte[16]));

new EncryptedPrivateKeyInfo(algorithmParameters, encryptedPrivateKey); // line 30
