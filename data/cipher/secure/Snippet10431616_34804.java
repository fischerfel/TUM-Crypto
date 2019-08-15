KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
keygenerator.initialize(1024, random);

KeyPair keypair = keygenerator.generateKeyPair();
PrivateKey privateKey = keypair.getPrivate();
PublicKey publicKey = keypair.getPublic();
Cipher cipher = Cipher.getInstance("RSA");

String arrayStr = "[b@d7eed7";
byte ciphertext = arrayStr.getBytes();
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] cleartext1 = cipher.doFinal(ciphertext);
System.out.println("the decrypted cleartext is: " + new String(cleartext1));
