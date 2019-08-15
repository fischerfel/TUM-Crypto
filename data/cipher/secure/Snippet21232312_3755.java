Security.addProvider(new BouncyCastleProvider());

    KeyPairGenerator kpg = (KeyPairGenerator) KeyPairGenerator.getInstance("ECIES", "BC");

    kpg.initialize(192, new SecureRandom());

    KeyPair keyPair = kpg.generateKeyPair();
    PublicKey pubKey = keyPair.getPublic();
    PrivateKey privKey = keyPair.getPrivate();

    byte[] d = new byte[]{1, 2, 3, 4, 5, 6, 7, 8}; // 1. can someone tell me what this parameters does?
    byte[] e = new byte[]{8, 7, 6, 5, 4, 3, 2, 1};

    IESParameterSpec param = new IESParameterSpec(d, e, 192); // 2. and this parameters?
    IEKeySpec c1Key = new IEKeySpec(privKey, pubKey);
    System.out.println(c1Key.getPublic());

    Cipher cipher = Cipher.getInstance("ECIES", "BC");
    cipher.init(Cipher.ENCRYPT_MODE, c1Key, param);
    System.out.println(cipher.doFinal("test12345678900987654321".getBytes()));
