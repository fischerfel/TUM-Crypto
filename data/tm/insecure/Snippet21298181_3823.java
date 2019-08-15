private ConsumerManager getFreeHttpsManager() throws NoSuchAlgorithmException, KeyManagementException {
    //ignore all
    TrustManager trm = new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {

        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    };

    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, new TrustManager[]{trm}, null);
    //  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    X509HostnameVerifier verifier = new X509HostnameVerifier() {

        @Override
        public void verify(String string, SSLSocket ssls) throws IOException {
        }

        @Override
        public void verify(String string, X509Certificate xc) throws SSLException {
        }

        @Override
        public void verify(String string, String[] strings, String[] strings1) throws SSLException {
        }

        @Override
        public boolean verify(String string, SSLSession ssls) {
            return true;
        }
    };
    Discovery discovery = new Discovery();
    discovery.setYadisResolver(new YadisResolver(new HttpFetcherFactory(sc, verifier)));
    return new ConsumerManager(
            new RealmVerifierFactory(new YadisResolver(new HttpFetcherFactory(sc, verifier))),
            discovery,  // uses HttpCache internally
            new HttpFetcherFactory(sc, verifier));
}
