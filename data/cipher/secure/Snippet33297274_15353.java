// add instance of provider class
Security.addProvider(new BouncyCastleProvider());

// initializing parameter specs secp256r1/prime192v1
ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("prime192v1");

// key pair generator to generate public and private key
KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", new BouncyCastleProvider());

// initialize key pair generator
generator.initialize(ecSpec);

// Key pair to store public and private key
KeyPair keyPair = generator.generateKeyPair();

Cipher iesCipher = Cipher.getInstance("ECIES", new BouncyCastleProvider());
iesCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
