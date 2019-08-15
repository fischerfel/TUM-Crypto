Cipher cipher = Cipher.getInstance("RSA");  
KeyFactory fact = KeyFactory.getInstance("RSA"); 
KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
keyPairGenerator.initialize(1024); // 1024 used for normal

KeyPair keyPair = keyPairGenerator.generateKeyPair();
PublicKey publicKey = keyPair.getPublic();
PrivateKey privateKey = keyPair.getPrivate();

FileOutputStream fos = null;
ObjectOutputStream oos = null;
