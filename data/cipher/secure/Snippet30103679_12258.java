ECPublicKey publicKey ;
ECPrivateKey privateKey;

//Generating key paire (public and private keys) 
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "SunEC");
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

    keyGen.initialize(571, random);
    KeyPair pair = keyGen.generateKeyPair();
    privateKey = (ECPrivateKey) pair.getPrivate();
    publicKey = (ECPublicKey) pair.getPublic();

// get an AES cipher object with CTR encription mode 
   Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

// encrypt the sharedSecret using the public key
   cipher.init(Cipher.ENCRYPT_MODE, publicKey);**
   byte[] result = cipher.doFinal(data);
