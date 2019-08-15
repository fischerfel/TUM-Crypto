    // prepare Verifiers that will accept certificate from ANY host
    yesHostnameVerifier = new CYesHostnameVerifier();

    // initiate the SSL context and prepare trust manager that will accept ANY certificate
    TrustManager yesTrustManager    = new CYesTrustManager();
    TrustManager[] yesTrustManagers = new TrustManager[] {yesTrustManager};
    try {
        SSLContext yesSslcontext = SSLContext.getInstance("TLS");
        yesSslcontext.init(null, yesTrustManagers, null); // key manager, trust manager, secure random manager
        yesSslFactory = yesSslcontext.getSocketFactory();
    } catch (Exception e) {
    }   

    // configure the HTTPS connection
    HttpsURLConnection.setDefaultSSLSocketFactory(yesSslFactory);
    HttpsURLConnection.setDefaultHostnameVerifier(yesHostnameVerifier);
