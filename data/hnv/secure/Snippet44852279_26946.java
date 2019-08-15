class CustomHttpClient extends DefaultHttpClient {

    private final String alias;
    private final X509Certificate[] certificates;
    private final PrivateKey privateKey;

    public CustomHttpClient(String alias, X509Certificate[] certificates, PrivateKey privateKey) {
        this.alias = alias;
        this.certificates = certificates;
        this.privateKey = privateKey;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", newSslSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), schemeRegistry);
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("pkcs12");
            trusted.load(null);
            if (this.alias != null) {
                trusted.setKeyEntry(this.alias, privateKey, Constants.KEYSTORE_PASS.toCharArray(), this.certificates);
            }

            SSLSocketFactory socketFactory = new SSLSocketFactory(trusted, Constants.KEYSTORE_PASS);
            socketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return socketFactory;
        } catch (Exception ex) {
            throw new AssertionError(ex);
        }
    }

}
