// add instance of provider class
Security.addProvider(new BouncyCastleProvider());

String name = "secp256r1";

// NOTE just "EC" also seems to work here
KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);
kpg.initialize(new ECGenParameterSpec(name));

// Key pair to store public and private key
KeyPair keyPair = kpg.generateKeyPair();

Cipher iesCipher = Cipher.getInstance("ECIES", BouncyCastleProvider.PROVIDER_NAME);
iesCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
