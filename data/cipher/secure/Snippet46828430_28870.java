KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(2048);
KeyPair keyPair = kpg.generateKeyPair();

PublicKey puKey = keyPair.getPublic();
PrivateKey prKey = keyPair.getPrivate()

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.PUBLIC_KEY, puKey);
byte[] encryptedKey = cipher.doFinal(byteCipherText);
