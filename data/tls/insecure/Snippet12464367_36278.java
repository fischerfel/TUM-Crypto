SSLContext sc = SSLContext.getInstance("SSLv3");

KeyManagerFactory kmf =
    KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );

KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
ks.load(new FileInputStream( certPath ), certPasswd.toCharArray() );

kmf.init( ks, certPasswd.toCharArray() );

sc.init( kmf.getKeyManagers(), null, null );

((BindingProvider) webservicePort).getRequestContext()
    .put(
        "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",
        sc.getSocketFactory() );
