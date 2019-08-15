byte[] rawFile;
File f = new File("./src/wonkybox.stl");
FileInputStream fileReader = new FileInputStream(f);
rawFile = new byte[(int)f.length()];
fileReader.read(rawFile);

/*****   Encrypt the file (CAN DO THIS ONCE!)  ***********/

//Generate the public/private keys
KeyPairGenerator keyGen = KeyPairGenerator.getInstance("AES");
SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
keyGen.initialize(1024, random);
KeyPair key = keyGen.generateKeyPair();
PrivateKey privKey = key.getPrivate();
PublicKey pubKey = key.getPublic();

//Store the keys
byte[] pkey = pubKey.getEncoded();
FileOutputStream keyfos = new FileOutputStream("./CloudStore/keys/pubkey");
keyfos.write(pkey);
keyfos.close();

pkey = privKey.getEncoded();
keyfos = new FileOutputStream("./CloudStore/keys/privkey");
keyfos.write(pkey);
keyfos.close();


//Read public/private keys
KeyFactory keyFactory = KeyFactory.getInstance("AES");
FileInputStream keyfis = new FileInputStream("./CloudStore/keys/pubkey");
byte[] encKey = new byte[keyfis.available()];
keyfis.read(encKey);
keyfis.close();

X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
PublicKey pub1Key = keyFactory.generatePublic(pubKeySpec);

keyfis = new FileInputStream("./CloudStore/keys/privkey");
encKey = new byte[keyfis.available()];
keyfis.read(encKey);
keyfis.close();

PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
PrivateKey priv1key = keyFactory.generatePrivate(privKeySpec);

//Encrypt file using public key
Cipher cipher = Cipher.getInstance("AES");
System.out.println("provider= " + cipher.getProvider());
cipher.init(Cipher.ENCRYPT_MODE, pub1Key);


byte[] encryptedFile;
encryptedFile = cipher.doFinal(rawFile);

//Write encrypted file to 'CloudStore' folder
FileOutputStream fileEncryptOutput = new FileOutputStream(new File("./CloudStore/encrypted.txt"));
fileEncryptOutput.write(encryptedFile);
fileEncryptOutput.close();
