public static SchemeRegistry buildSchemeRegistry() throws Exception {
    final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(createKeyManager(), createTrustManager(), new SecureRandom());
    final SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("https", 443, new SSLSocketFactory(sslContext)));
    return schemeRegistry;
}
