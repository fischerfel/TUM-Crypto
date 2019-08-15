static private SSLSocketFactory newSslSocketFactory(String jksFile, char[] password) {

    try {
        KeyStore trusted = KeyStore.getInstance("JKS");
        InputStream in = StaticHttpsClient.class.getClassLoader().getResourceAsStream(jksFile);
        try {
            trusted.load(in, password);
        } finally {
            in.close();
        }

        SSLSocketFactory socketFactory = new SSLSocketFactory(trusted);
        HostnameVerifier hostnameVerifier =    org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        socketFactory .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        return socketFactory;
    } catch (Exception e) {
        throw new AssertionError(e);
    }
}

static protected ClientConnectionManager createClientConnectionManager(String jksFile, char[] password) {
    SchemeRegistry registry = new SchemeRegistry();

    registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
    registry.register(new Scheme("https", 443, newSslSocketFactory(jksFile, password)));
    return new SingleClientConnManager(registry);

}   
