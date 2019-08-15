    Security.addProvider(new BouncyCastleProvider());
    byte[] input = "Encryption Test".getBytes();
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC"); // init Cipher to use AES/CBC
    SecureRandom random = new SecureRandom(); // IV is random 
    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC"); // Key will be generated with RSA

    generator.initialize(512, random);
    KeyPair pair = generator.generateKeyPair();
    Key pubKey = pair.getPublic();
    //Key privKey = pair.getPrivate();

    cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
