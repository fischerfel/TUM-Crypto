keyGen.initialize(1024);
KeyPair keypair = keyGen.genKeyPair();
PrivateKey privateKey = keypair.getPrivate();
PublicKey publicKey = keypair.getPublic();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] cipherData = cipher.doFinal(initkey);
// Second level of encryption
KeyPairGenerator keyGen1 = KeyPairGenerator.getInstance("RSA");
keyGen1.initialize(1024);
KeyPair keypair1 = keyGen1.genKeyPair();
PrivateKey prvKey = keypair1.getPrivate();
PublicKey pubKey = keypair1.getPublic();
Cipher cipher1 = Cipher.getInstance("RSA");
cipher1.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] cipherData_new = cipher1.doFinal(cipherData);
