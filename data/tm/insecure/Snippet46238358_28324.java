private void testFeign() {
    SSLContext sslContext = getSSlContext();
    Client myClient = new Client.Default(sslContext.getSocketFactory(), gethostnameVerifier());
    return Feign.builder().client(myClient).encoder(new GsonEncoder()).decoder(new GsonDecoder())
            .logger(new Slf4jLogger(type)).logLevel(Logger.Level.FULL).target(type, uri);
}

private static HostnameVerifier gethostnameVerifier() {
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };
    return hostnameVerifier;
}

private static SSLContext getSSlContext() {

    final TrustManager[] trustAllCerts = new TrustManager[] { getTrustManager() };
    SSLContext sslContext = null;
    try {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);

    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return sslContext;
}

private static X509TrustManager getTrustManager() {

    final X509TrustManager trustManager = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };
    return trustManager;
}
