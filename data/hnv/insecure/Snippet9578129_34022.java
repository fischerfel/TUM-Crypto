public HttpClientVM() {

    BasicHttpParams params = new BasicHttpParams();
    ConnManagerParams.setMaxTotalConnections(params, 10);
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setUseExpectContinue(params, false);
    HttpConnectionParams.setStaleCheckingEnabled(params, true);
    HttpConnectionParams.setConnectionTimeout(params, 30000);
    HostnameVerifier hostnameVerifier=
          org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http",socketFactory, 80));
    schemeRegistry.register(new Scheme("https",socketFactory, 443));
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
        // Set verifier     
        client = new DefaultHttpClient(manager, params);    
    }
