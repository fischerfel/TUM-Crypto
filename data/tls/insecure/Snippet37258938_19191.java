private static SSLContext getSSLContext(final String keystore) throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLSv1");
    final char[] passphrase = "changeit".toCharArray();
    final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    final KeyStore ks = KeyStore.getInstance("JKS");
    try (InputStream stream = Main.class.getResourceAsStream(keystore)) {
        ks.load(stream, passphrase);
    }
    kmf.init(ks, passphrase);
    sslContext.init(kmf.getKeyManagers(), null, null);
    return sslContext;
}

final ResourceConfig rc = new ResourceConfig().packages("path.to.package");
final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
final NetworkListener listener = server.getListeners().iterator().next();
listener.registerAddOn(new Http2AddOn());
listener.setSecure(true);
final SSLEngineConfigurator configurator =
    new SSLEngineConfigurator(
        getSSLContext("keystore.jks"),
        false,
        false,
        false
    );
listener.setSSLEngineConfig(configurator);
