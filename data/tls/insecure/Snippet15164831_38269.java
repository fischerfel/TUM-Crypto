    private DefaultHttpClient creatHttpClient(KeyStore keyStore, 
        char[] keyStorePassword, KeyStore trustStore, char[] trustStorePassword) {
    try {

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore); // This contains CA certs
        TrustManager[] tm = trustManagerFactory.getTrustManagers();

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword); // This contain client private key
        KeyManager[] km = keyManagerFactory.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(km, tm, new SecureRandom());

        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext, 
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 30000);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", 
                443, sslSocketFactory));

        ClientConnectionManager clientConnectionManager = 
                new ThreadSafeClientConnManager(schemeRegistry);

        final DefaultHttpClient httpClient = new DefaultHttpClient(
                clientConnectionManager, params);
        return httpClient;
    } catch(Exception e) {
        throw e;
    }
}
