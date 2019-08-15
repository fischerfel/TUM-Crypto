private DefaultHttpClient createHttpsClient(){
    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new SelfCertificatesSocketFactory(trustStore);
        //sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, sf));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
        return new DefaultHttpClient(ccm);
    } catch (Exception e) {
        return new DefaultHttpClient();
    }

}
