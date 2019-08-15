KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES");
kpg.initialize(new ECGenParameterSpec("secp256r1"));
KeyPair keyPair = kpg.generateKeyPair();

Cipher cipher = Cipher.getInstance("ECIES");
cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

String toEncrypt = "Hello";

// Check that cipher works ok
cipher.doFinal(toEncrypt.getBytes());

// Using a SealedObject to encrypt the same string fails with a NullPointerException
SealedObject sealedObject = new SealedObject(toEncrypt, cipher);
