    HttpClient client = new DefaultHttpClient();
    SSLContext sslContext = SSLContext.getInstance("TLS");      

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance("JKS");
    File trustFile = new File("clientTrustStore.jks");
    ks.load(new FileInputStream(trustFile), null);
    tmf.init(ks);
    sslContext.init(null, tmf.getTrustManagers(),null);  
    SSLSocketFactory sf = new SSLSocketFactory(sslContext); 
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    Scheme scheme = new Scheme("https", sf, 443);
    client.getConnectionManager().getSchemeRegistry().register(scheme);
    httpGet = new HttpGet("https://localhost:8443/myApp");
    HttpResponse httpResponse = client.execute(httpGet);
