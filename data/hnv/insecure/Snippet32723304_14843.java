private static ClientConnectionManager newConnectionManager() throws Exception {
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null, null);
    SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", sf, 443));
    ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(registry);
    cm.setMaxTotal(Integer.parseInt(System.getProperty("zuul.max.host.connections", "200")));
    cm.setDefaultMaxPerRoute(Integer.parseInt(System.getProperty("zuul.max.host.connections", "20")));
    return cm;
}
