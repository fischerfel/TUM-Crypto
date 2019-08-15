    SSLContext sc = SSLContext.getInstance("TLS");

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance("PKCS12");
    ks.load(new FileInputStream(".....p12"), "password".toCharArray());
    kmf.init(ks, "password".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(new FileInputStream("....jks"), "password2".toCharArray());
    tmf.init(ts);

    sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    com.services.Data service = new com.services.Data();
    com.services.DataService port = service.getDataServiceImplPort();
    BindingProvider bp = (BindingProvider) port;
    Map map = bp.getRequestContext();
   map.put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory", sc.getSocketFactory());
    map.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", sc.getSocketFactory());

    res = port.getInformation();
