public static void main(String[] args) throws Exception {
    Endpoint endpoint = Endpoint.create(new RapidCommandService());
    SSLContext ssl =  SSLContext.getInstance("SSLv3");

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
    KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());

    getLog().debug ( SHORT_NAME + ".main() - Java User Directory..........................[" + System.getProperty ( "user.dir" ) + "]" );
    getLog().debug ( SHORT_NAME + ".main() - Java Home Directory..........................[" + System.getProperty ( "java.home" ) + "]" );
    getLog().debug ( SHORT_NAME + ".main() - Java Version.................................[" + System.getProperty ( "java.version" ) + "]" );

    //Load the JKS file (located, in this case, at D:\keystore.jks, with password 'test'
    store.load(new FileInputStream("C:\\usr\\apps\\java\\jre-170-65\\lib\\security\\rapid-command-service.jks"), "changeit".toCharArray()); 

    //init the key store, along with the password 'test'
    kmf.init(store, "changeit".toCharArray());
    KeyManager[] keyManagers = new KeyManager[1];
    keyManagers = kmf.getKeyManagers();

    //Init the trust manager factory
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

    //It will reference the same key store as the key managers
    tmf.init(store);

    TrustManager[] trustManagers = tmf.getTrustManagers();

    ssl.init(keyManagers, trustManagers, new SecureRandom());
    getLog().debug ( SHORT_NAME + ".main() - Java SSL Truststore..........................[" + System.getProperty ( "javax.net.ssl.trustStore" ) + "]" );
    getLog().debug ( SHORT_NAME + ".main() - Java SSL Keystore............................[" + System.getProperty ( "javax.net.ssl.keyStore" ) + "]" );

    //Init a configuration with our SSL context
    HttpsConfigurator configurator = new HttpsConfigurator(ssl);
    System.setProperty("javax.net.debug", "ssl");

    //Create a server on localhost, port 443 (https port)
    HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress("localhost", 443), 443);
    httpsServer.setHttpsConfigurator(configurator);


    //Create a context so our service will be available under this context
    HttpContext context = httpsServer.createContext("/rapidCommandService");
    httpsServer.start();

    //Finally, use the created context to publish the service
    endpoint.publish(context);

}
