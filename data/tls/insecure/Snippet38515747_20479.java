private static HttpsServer httpsServer;

@BeforeClass
public static void server1() throws Exception {
    httpsServer = HttpsServer.create(new InetSocketAddress(8181), 0);

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

    Path keyStorePath = Paths.get(System.getProperty(JAVA_HOME), KEY_STORE_PATH);
    keyStore.load(Files.newInputStream(keyStorePath),
            KEY_STORE_PASSWORD.toCharArray());

    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance (KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init ( keyStore, "changeit".toCharArray());

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

    httpsServer.setHttpsConfigurator ( new HttpsConfigurator( sslContext )
    {
        public void configure ( HttpsParameters params )
        {
            try
            {
                // initialise the SSL context
                SSLContext c = SSLContext.getDefault ();
                SSLEngine engine = c.createSSLEngine ();
                params.setNeedClientAuth ( false );
                params.setCipherSuites ( engine.getEnabledCipherSuites () );
                params.setProtocols ( engine.getEnabledProtocols () );

                // get the default parameters
                SSLParameters defaultSSLParameters = c.getDefaultSSLParameters ();
                params.setSSLParameters ( defaultSSLParameters );
            }
            catch ( Exception ex )
            {
                System.out.println( "Failed to create HTTPS port" );
            }
        }
    } );

    //Create a default executor
    httpsServer.setExecutor(Executors.newCachedThreadPool());
    httpsServer.start();
}

@AfterClass
public static void shutdown(){
    httpsServer.stop(0);
}

@Test
public void testNotLetsEncryptButSignedByCAs() throws Exception {
    URLConnection connection = new URL("https://localhost:8181").openConnection();
    connection.connect();
    System.out.println(connection.getHeaderFields());
}
