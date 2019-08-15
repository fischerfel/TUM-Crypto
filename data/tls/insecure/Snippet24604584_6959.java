public static ServerSocket getServerSocket(int port) {
    System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
    ServerSocket serverSocket = null;
    secureRandom = new SecureRandom();
    Thread.currentThread().sleep(30000);
    secureRandom.nextInt();

    setupClientKeyStore();
    setupServerKeystore();
    setupSSLContext();

    SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
    serverSocket = sf.createServerSocket(port);
    ((SSLServerSocket)serverSocket).setNeedClientAuth(true);
    }
    return serverSocket;
}

private static void setupClientKeyStore() throws GeneralSecurityException, IOException   {
    clientKeyStore = KeyStore.getInstance("JKS");

    KeyStore client = KeyStore.getInstance("JKS");
    client.load( new FileInputStream("client.public"), "public".toCharArray() );

    KeyStore client1 = KeyStore.getInstance( "JKS" );
    client1.load( new FileInputStream("client1.public"), "public".toCharArray() );

    KeyStore.Entry clientpublic = client.getEntry("clientpublic", null);
    KeyStore.Entry client1public = client1.getEntry("client1public", null);

    clientKeyStore.load(null, null);
    clientKeyStore.setEntry("clientpublic", clientpublic, null);
    clientKeyStore.setEntry("client1public", client1public, null);

}

private static void setupServerKeystore() throws GeneralSecurityException, IOException       {
    serverKeyStore = KeyStore.getInstance( "JKS" );
    serverKeyStore.load( new FileInputStream( "server.private" ),
            passphrase.toCharArray() );
}

private static void setupSSLContext() throws GeneralSecurityException, IOException {
    TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
    tmf.init( clientKeyStore );

    KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
    kmf.init( serverKeyStore, passphrase.toCharArray() );

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init( kmf.getKeyManagers(),
            tmf.getTrustManagers(),
            secureRandom);
}
