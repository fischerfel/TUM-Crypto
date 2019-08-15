    System.setProperty("javax.net.debug", "ssl,handshake");
    System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");

    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(loadStream("C:/TrustStore/cacerts"), "trustpass".toCharArray());
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(loadStream("C:/KeyStore/SSL/keystore.SomeKey"), "keypass".toCharArray());

    SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(ts).loadKeyMaterial(ks, "somekey".toCharArray()).setSecureRandom(new SecureRandom());        
    SSLContext ssl = sslBuilder.build();

    PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));        

    CloseableHttpAsyncClient clientHttps = HttpAsyncClientBuilder.create()
            .setConnectionManager(cm)    
            .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            .setSSLContext(ssl)
            .build();

    RequestConfig.Builder b = RequestConfig.custom();        
    b.setProxy(new HttpHost("proxyHost", proxyPort));
    RequestConfig rc = b.build();

    clientHttps.start();

    HttpRequestBase req = new HttpPost("https://someurl");
    ((HttpEntityEnclosingRequestBase)req).setEntity(new StringEntity("somestring"));
    req.setConfig(rc);

    clientHttps.execute(req, new FutureCallback<HttpResponse>() {

        @Override
        public void failed(Exception ex) {
            System.out.println(ex);
        }

        @Override
        public void completed(HttpResponse result) {
            System.out.println(result);                
        }

        @Override
        public void cancelled() {
            System.out.println("Cancelled");                
        }
    });    
