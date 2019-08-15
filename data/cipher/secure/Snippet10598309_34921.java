KeyPairGenerator keyGen;
keyGen = KeyPairGenerator.getInstance("RSA");
keyGen.initialize(1024);
KeyPair key = keyGen.generateKeyPair();

String publicKeyPath = new String("publicKeys.txt");
publickey = key.getPublic()
byte[] pubEncoded = key.getPublic().getEncoded();
FileOutputStream fout = new FileOutputStream(publicKeyPath);
fout.write(pubEncoded);
fout.flush();
fout.close();

String privateKeyPath = new String("privateKeys.txt");
byte[] privEncoded = key.getPrivate().getEncoded();
fout = new FileOutputStream(privateKeyPath);
fout.write(privEncoded);
fout.flush();
fout.close();

cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
