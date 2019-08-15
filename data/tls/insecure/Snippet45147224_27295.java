    SSLContext sc = SSLContext.getInstance("TLS");
    KeyManagerFactory kmf =    KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
    KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
    ks.load(new FileInputStream( "C:\\Users\\amikaml\\.keystore" ), certPassword.toCharArray() );
    kmf.init( ks, certPassword.toCharArray() );
    sc.init( kmf.getKeyManagers(), null, null );
     binding.getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",sc.getSocketFactory() );
