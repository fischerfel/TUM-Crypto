Security.addProvider(new BouncyCastleProvider());
    ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("prime256v1");
    try 
    {
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA","BC");
        g.initialize(spec, new SecureRandom());
        KeyPair keyPair = g.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        System.out.println("PublicKey:"+publicKey+"\n");
        System.out.println("PrivateKey:"+privateKey+"\n");

    } 
    catch (Exception e) 
    {
        e.printStackTrace();
    }
    String origin = "This is origin string";
    try {
        Cipher c = Cipher.getInstance("ECIES","BC");
        c.init(Cipher.ENCRYPT_MODE,publicKey);
        encodeBytes = c.doFinal(origin.getBytes());

        String encrypt = encodeBytes.toString();
        System.out.println("Encrypt:"+ encrypt+"\n");
    } catch (Exception e) {
        e.printStackTrace();
    }
