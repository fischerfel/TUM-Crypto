    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024, new SecureRandom());
    KeyPair keypair = keyGen.generateKeyPair();

    System.setProperty("javax.net.ssl.keyStore", System.getProperty("user.home")
        + File.separator +
            + "/keystore.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", "xyz");

    KeyManagerFactory keyManagerFactory = KeyManagerFactory
            .getInstance("SunX509");
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(null, "xyz".toCharArray());

    //setKeyEntry parameter 3 can not be null: 
    //IllegalArgumentException: Private key must be accompanied by certificate chain
    keyStore.setKeyEntry("alias", keypair.getPrivate(),
            "xyz".toCharArray(), null);

    keyManagerFactory.init(keyStore, "xyz".toCharArray());
    // keyStore.load
    SSLContext context = SSLContext.getInstance("TLS");// "SSLv3"
    context.init(keyManagerFactory.getKeyManagers(), null,
            new SecureRandom());
    ServerSocketFactory socketFactory = context.getServerSocketFactory();
    ServerSocket ssocket = socketFactory.createServerSocket(1443);
    Socket socket = ssocket.accept();
